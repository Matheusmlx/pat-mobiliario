package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.relatorios.memorando.memorandomovimentacaofinalizada;

import br.com.azi.patrimoniomobiliario.domain.commons.movimentacao.memorando.MemorandoMovimentacaoInternaUtils;
import br.com.azi.patrimoniomobiliario.domain.constant.movimentacao.TipoMovimentacaoEnum;
import br.com.azi.patrimoniomobiliario.domain.entity.Arquivo;
import br.com.azi.patrimoniomobiliario.domain.entity.ContaContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.LancamentoContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.MemorandoMovimentacaoInterna;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.MovimentacaoItem;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.LancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeRelatoriosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.finalizacao.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.relatorios.memorando.memorandomovimentacaofinalizada.converter.GerarMemorandoMovimentacaoInternaFinalizadaOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.relatorios.memorando.memorandomovimentacaofinalizada.exception.GerarMemorandoMovimentacaoInternaFinalizadaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.relatorios.memorando.memorandomovimentacaofinalizada.exception.LancamentoContabilPorPatrimonioException;
import br.com.azi.patrimoniomobiliario.utils.date.DateUtils;
import br.com.azi.patrimoniomobiliario.utils.string.StringUtils;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
public class GerarMemorandoMovimentacaoInternaFinalizadaUseCaseImpl implements GerarMemorandoMovimentacaoInternaFinalizadaUseCase {

    private final MovimentacaoDataProvider movimentacaoDataProvider;

    private final MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    private final LancamentoContabilDataProvider lancamentoContabilDataProvider;

    private final SistemaDeRelatoriosIntegration sistemaDeRelatoriosIntegration;

    private final MemorandoMovimentacaoInternaUtils memorandoMovimentacaoInternaUtils;

    private final GerarMemorandoMovimentacaoInternaFinalizadaOutputDataConverter outputDataConverter;

    @Override
    public GerarMemorandoMovimentacaoInternaFinalizadaOutputData executar(GerarMemorandoMovimentacaoInternaFinalizadaInputData inputData) {
        validarDadosEntrada(inputData);
        Movimentacao movimentacao = buscarMovimentacao(inputData);
        validarSeMovimentacaoFinalizado(movimentacao);

        List<Long> patrimoniosId = buscarPatrimoiosDaMovimentacao(movimentacao);

        List<LancamentoContabil>  lancamentosContabeis = buscarLancamentosContabeisDePatrimoniosPorMovimentacao(patrimoniosId, movimentacao);
        List<MemorandoMovimentacaoInterna.ContaContabil> itensContasContabeis = prepararContasContabeisParaMemorando(lancamentosContabeis, movimentacao);
        atualizarTotalizadoresPorContaContabil(itensContasContabeis);

        Arquivo arquivo = sistemaDeRelatoriosIntegration.gerarMemorandoMovimentacaoInterna(montarMemorando(movimentacao, itensContasContabeis));

        return outputDataConverter.to(arquivo);
    }

    private void validarDadosEntrada(GerarMemorandoMovimentacaoInternaFinalizadaInputData inputData) {
        Validator.of(inputData)
            .validate(GerarMemorandoMovimentacaoInternaFinalizadaInputData::getIdMovimentacao, Objects::nonNull, "O Id é nulo")
            .get();
    }

    private Movimentacao buscarMovimentacao(GerarMemorandoMovimentacaoInternaFinalizadaInputData inputData) {
        Optional<Movimentacao> movimentacao = movimentacaoDataProvider.buscarPorId(inputData.getIdMovimentacao());
        return movimentacao.orElseThrow(MovimentacaoNaoEncontradaException::new);
    }

    private void validarSeMovimentacaoFinalizado(Movimentacao movimentacao) {
        if (!movimentacao.getSituacao().equals(Movimentacao.Situacao.FINALIZADO) && !movimentacao.getSituacao().equals(Movimentacao.Situacao.DEVOLVIDO)) {
            throw new GerarMemorandoMovimentacaoInternaFinalizadaException("Não é possível gerar termo para movimentações não finalizadas.");
        }
    }

    List<MemorandoMovimentacaoInterna.ContaContabil> prepararContasContabeisParaMemorando(List<LancamentoContabil> lancamentoContabeis, Movimentacao movimentacao) {
        List<ContaContabil> contasContabeis =  encontrarContasContabeis(lancamentoContabeis);
        List<MemorandoMovimentacaoInterna.ContaContabil> contasContabeisMemorando = new ArrayList<>();

        for (ContaContabil contaContabil: contasContabeis) {
            List<Patrimonio> patrimoniosNaContaContabil = filtrarPatrimoniosPorContaContabil(lancamentoContabeis, contaContabil);
            contasContabeisMemorando.add(
                MemorandoMovimentacaoInterna.ContaContabil
                    .builder()
                    .id(contaContabil.getId())
                    .descricao(memorandoMovimentacaoInternaUtils.formatarContaContabil(contaContabil))
                    .patrimonios(converterPatrimoniosParaRelatorio(patrimoniosNaContaContabil, lancamentoContabeis, movimentacao))
                    .build());
        }
        return contasContabeisMemorando;
    }

    private List<ContaContabil> encontrarContasContabeis(List<LancamentoContabil> registros) {
        List<ContaContabil> contasContabeis = new ArrayList<>();
        for (LancamentoContabil lancamentoContabil: registros) {
            if (!contasContabeis.contains(lancamentoContabil.getContaContabil())){
                contasContabeis.add(lancamentoContabil.getContaContabil());
            }
        }
        return contasContabeis;
    }

    private List<Patrimonio> filtrarPatrimoniosPorContaContabil(List<LancamentoContabil> lancamentoContabeis, ContaContabil contaContabil) {
        List<Patrimonio> patrimonios = new ArrayList<>();
        for (LancamentoContabil lancamentoContabil: lancamentoContabeis) {
            if (lancamentoContabil.getContaContabil().getId().equals(contaContabil.getId()) && !patrimonios.contains(lancamentoContabil.getPatrimonio())) {
                patrimonios.add(lancamentoContabil.getPatrimonio());
            }
        }
        return patrimonios;
    }

    private void atualizarTotalizadoresPorContaContabil(List<MemorandoMovimentacaoInterna.ContaContabil> contasContabeis) {
        for (MemorandoMovimentacaoInterna.ContaContabil contaContabil: contasContabeis) {
            contaContabil.setValorDepreciadoAcumulado(calcularDepreciacaoAcumuladaPorContaContabil(contaContabil));
            contaContabil.setValorEntrada(calcularValorEntradaPorContaContabil(contaContabil));
            contaContabil.setValorLiquido(calcularValorLiquidoPorContaContabil(contaContabil));
        }
    }

    private BigDecimal calcularDepreciacaoAcumuladaPorContaContabil(MemorandoMovimentacaoInterna.ContaContabil contaContabil) {
        BigDecimal amortizacaoAcumuladaTotal = new BigDecimal("0").setScale(2, RoundingMode.HALF_EVEN);
        for (MemorandoMovimentacaoInterna.Patrimonio patrimonio: contaContabil.getPatrimonios()) {
                amortizacaoAcumuladaTotal = amortizacaoAcumuladaTotal.add(patrimonio.getValorDepreciadoAcumulado(), new MathContext(10)).setScale(2, RoundingMode.HALF_EVEN);
        }
        return amortizacaoAcumuladaTotal;
    }

    private BigDecimal calcularValorEntradaPorContaContabil(MemorandoMovimentacaoInterna.ContaContabil contaContabil) {
        BigDecimal valorEntradaTotal =  new BigDecimal("0").setScale(2, RoundingMode.HALF_EVEN);

        for (MemorandoMovimentacaoInterna.Patrimonio patrimonio: contaContabil.getPatrimonios()) {
            valorEntradaTotal = valorEntradaTotal.add(patrimonio.getValorEntrada()).setScale(2, RoundingMode.HALF_EVEN);
        }
        return valorEntradaTotal;
    }

    private BigDecimal calcularValorLiquidoPorContaContabil(MemorandoMovimentacaoInterna.ContaContabil contaContabil) {
        BigDecimal valorLiquidoTotal = new BigDecimal("0").setScale(2, RoundingMode.HALF_EVEN);

        for (MemorandoMovimentacaoInterna.Patrimonio patrimonio: contaContabil.getPatrimonios()) {
            valorLiquidoTotal = valorLiquidoTotal.add(patrimonio.getValorLiquido(), new MathContext(10)).setScale(2, RoundingMode.HALF_EVEN);
        }
        return valorLiquidoTotal;
    }

    private LancamentoContabil filtrarLancamentoContabilParaPatrimonio(List<LancamentoContabil> lcs, Patrimonio patrimonio) {
        return lcs
            .stream()
            .filter(map -> patrimonio.getId().equals(map.getPatrimonio().getId()))
            .findFirst()
            .orElseThrow(LancamentoContabilPorPatrimonioException::new);
    }

    private List<MemorandoMovimentacaoInterna.Patrimonio> converterPatrimoniosParaRelatorio(List<Patrimonio> patrimonios, List<LancamentoContabil> lancamentosContabeis, Movimentacao movimentacao) {
        List<MemorandoMovimentacaoInterna.Patrimonio> registrosPatrimonios = new ArrayList<>();
        for (Patrimonio patrimonio: patrimonios) {
            LancamentoContabil lancamentoContabil = filtrarLancamentoContabilParaPatrimonio(lancamentosContabeis, patrimonio);
            registrosPatrimonios.add(
                MemorandoMovimentacaoInterna.Patrimonio
                    .builder()
                    .id(patrimonio.getId())
                    .numero(patrimonio.getNumero())
                    .descricao(patrimonio.getItemIncorporacao().getDescricao())
                    .valorEntrada(patrimonio.getValorEntrada())
                    .valorDepreciadoAcumulado(patrimonio.getValorEntrada().subtract(lancamentoContabil.getValor()))
                    .valorLiquido(lancamentoContabil.getValor())
                    .devolvidoNaData(buscarDataDevolucaoNaMovimentacao(patrimonio, movimentacao))
                    .build()
            );
        }
        return registrosPatrimonios;
    }

    private MovimentacaoItem buscarItemMovimentacao(Patrimonio patrimonio, Movimentacao movimentacao) {
        Optional<MovimentacaoItem> itemMovimentacao = movimentacaoItemDataProvider.buscarPorMovimentacaoEPatrimonio(movimentacao.getId(), patrimonio.getId());
        return itemMovimentacao.orElseThrow(GerarMemorandoMovimentacaoInternaFinalizadaException::new);
    }

    private String buscarDataDevolucaoNaMovimentacao(Patrimonio patrimonio, Movimentacao movimentacao) {
        MovimentacaoItem item = buscarItemMovimentacao(patrimonio, movimentacao);
        return DateUtils.formatarData(item.getDataDevolucao());
    }

    private List<LancamentoContabil>  buscarLancamentosContabeisDePatrimoniosPorMovimentacao(List<Long> patrimonios, Movimentacao movimentacao) {
        if (movimentacao.getTipo().equals(TipoMovimentacaoEnum.TEMPORARIA)) {
            return lancamentoContabilDataProvider.buscarCreditoNaVoltaPorPatrimoniosEMovimentacao(patrimonios, movimentacao.getId());
        }
        return lancamentoContabilDataProvider.buscarCreditoPorPatrimoniosEMovimentacao(patrimonios, movimentacao.getId());
    }

    private List<Long> buscarPatrimoiosDaMovimentacao(Movimentacao movimentacao) {
        List<MovimentacaoItem> itens = movimentacaoItemDataProvider.buscarPorMovimentacaoId(movimentacao.getId());
        return itens.stream()
            .map(item -> item.getPatrimonio().getId())
            .collect(Collectors.toList());
    }

    private MemorandoMovimentacaoInterna montarMemorando(Movimentacao movimentacao, List<MemorandoMovimentacaoInterna.ContaContabil> itensContasContabeis){
       MemorandoMovimentacaoInterna memorandoMovimentacaoInterna = MemorandoMovimentacaoInterna
           .builder()
            .numeroMemorando(gerarNumeroTermoResponsabilidade(movimentacao)+"/"+buscarAnoDaMovimentacao(movimentacao))
            .orgao(configurarUnidadeOrganizacional(movimentacao.getOrgaoOrigem()))
            .setorOrigem(configurarUnidadeOrganizacional(movimentacao.getSetorOrigem()))
            .setorDestino(configurarUnidadeOrganizacional(movimentacao.getSetorDestino()))
            .numeroProcesso(movimentacao.getNumeroProcesso())
            .dataEnvio(memorandoMovimentacaoInternaUtils.formatarDataEnvioMovimentacao(movimentacao))
            .dataFinalizacao(memorandoMovimentacaoInternaUtils.formatarDataFinalizacaoMovimentacao(movimentacao))
            .dataEsperadaDevolucao(memorandoMovimentacaoInternaUtils.formatarEsperadaDeDevolucao(movimentacao))
            .mesAnoEnvio(formatarMesAno(movimentacao))
            .numeroMovimentacao(movimentacao.getCodigo())
            .valorEntrada(memorandoMovimentacaoInternaUtils.somarValorEntradaTotalPorOrgao(itensContasContabeis))
            .valorDepreciadoAcumulado(memorandoMovimentacaoInternaUtils.somarDepreciadoAcumuladoTotalPorOrgao(itensContasContabeis))
            .valorLiquido(memorandoMovimentacaoInternaUtils.somarValorLiquidoTotalPorOrgao(itensContasContabeis))
            .contasContabeis(itensContasContabeis)
            .motivoObs(movimentacao.getMotivoObservacao())
            .tipo(memorandoMovimentacaoInternaUtils.formatarTituloMemorando(movimentacao.getTipo()))
            .emElaboracao(Boolean.FALSE)
            .possuiItens(Boolean.TRUE)
            .temporaria(validarSeMovimentacaoTemporaria(movimentacao))
            .build();

       memorandoMovimentacaoInterna.getContasContabeis().sort(Comparator.comparing(MemorandoMovimentacaoInterna.ContaContabil::getDescricao));
       return memorandoMovimentacaoInterna;
    }

    private Boolean validarSeMovimentacaoTemporaria(Movimentacao movimentacao) {
        return movimentacao.getTipo().equals(TipoMovimentacaoEnum.TEMPORARIA);
    }

    private String formatarMesAno(Movimentacao movimentacao) {
        return DateUtils.formatarDataEmMesAno(movimentacao.getDataFinalizacao());
    }

    private String configurarUnidadeOrganizacional(UnidadeOrganizacional unidadeOrganizacional) {
        return unidadeOrganizacional.getSigla()+" - "+unidadeOrganizacional.getNome();
    }

    private String gerarNumeroTermoResponsabilidade(Movimentacao movimentacao) {
        String numeroTermoResponsabilidade = movimentacao.getNumeroTermoResponsabilidade();
        if (Objects.isNull(movimentacao.getNumeroTermoResponsabilidade())){
            Optional<Movimentacao> movimentacaoComTermo = buscarUltimaNumeroTermoResponsabilidade();
            numeroTermoResponsabilidade = movimentacaoComTermo.map(this::gerarProximoNumero).orElseGet(this::geraPrimeiroNumero);
            atualizarNumeroTermoMovimentacao(movimentacao, numeroTermoResponsabilidade);
        }
      return numeroTermoResponsabilidade;
    }

    private String gerarProximoNumero(Movimentacao movimentacao) {
        int proximonumero = Integer.parseInt(movimentacao.getNumeroTermoResponsabilidade()) + 1;
        return completarNumero(Integer.toString(proximonumero));
    }

    private Optional<Movimentacao> buscarUltimaNumeroTermoResponsabilidade() {
        return movimentacaoDataProvider.buscarUltimoNumeroTermoResponsabilidade();
    }

    private void atualizarNumeroTermoMovimentacao(Movimentacao movimentacao, String numeroTermoResponsabilidade) {
        movimentacao.setNumeroTermoResponsabilidade(numeroTermoResponsabilidade);
        movimentacaoDataProvider.salvar(movimentacao);
    }

    private String geraPrimeiroNumero() {
        return completarNumero("1");
    }

    private String completarNumero(String string) {
        return StringUtils.padLeftZeros(string, Integer.parseInt("6"));
    }

    private String buscarAnoDaMovimentacao(Movimentacao movimentacao) {
        return String.valueOf(movimentacao.getDataFinalizacao().getYear());
    }

}
