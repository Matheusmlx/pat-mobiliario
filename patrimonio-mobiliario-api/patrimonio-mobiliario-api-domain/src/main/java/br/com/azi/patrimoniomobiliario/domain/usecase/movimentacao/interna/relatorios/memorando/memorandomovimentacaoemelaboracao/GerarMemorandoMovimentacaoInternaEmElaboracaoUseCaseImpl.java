package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.relatorios.memorando.memorandomovimentacaoemelaboracao;

import br.com.azi.patrimoniomobiliario.domain.commons.movimentacao.memorando.MemorandoMovimentacaoInternaUtils;
import br.com.azi.patrimoniomobiliario.domain.constant.movimentacao.TipoMovimentacaoEnum;
import br.com.azi.patrimoniomobiliario.domain.entity.Arquivo;
import br.com.azi.patrimoniomobiliario.domain.entity.ContaContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.MemorandoMovimentacaoInterna;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.MovimentacaoItem;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeRelatoriosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.relatorios.memorando.memorandomovimentacaoemelaboracao.converter.GerarMemorandoMovimentacaoInternaEmElaboracaoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.relatorios.memorando.memorandomovimentacaoemelaboracao.exception.GerarMemorandoMovimentacaoEmElaboracaoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.relatorios.memorando.memorandomovimentacaoemelaboracao.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.utils.string.StringUtils;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
public class GerarMemorandoMovimentacaoInternaEmElaboracaoUseCaseImpl implements GerarMemorandoMovimentacaoInternaEmElaboracaoUseCase {

    private final MovimentacaoDataProvider movimentacaoDataProvider;

    private final MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    private final SistemaDeRelatoriosIntegration sistemaDeRelatoriosIntegration;

    private final MemorandoMovimentacaoInternaUtils memorandoMovimentacaoInternaUtils;

    private final GerarMemorandoMovimentacaoInternaEmElaboracaoOutputDataConverter outputDataConverter;

    private static final String SEM_VALOR = "-";

    @Override
    public GerarMemorandoMovimentacaoInternaEmElaboracaoOutputData executar(GerarMemorandoMovimentacaoInternaEmElaboracaoInputData inputData) {
        validarDadosEntrada(inputData);
        Movimentacao movimentacao = buscarMovimentacao(inputData);
        validarSeMovimentacaoEmElaboracao(movimentacao);

        Arquivo arquivo = sistemaDeRelatoriosIntegration.gerarMemorandoMovimentacaoInterna(montarMemorando(movimentacao));
        return outputDataConverter.to(arquivo);
    }

    private void validarDadosEntrada(GerarMemorandoMovimentacaoInternaEmElaboracaoInputData inputData) {
        Validator.of(inputData)
            .validate(GerarMemorandoMovimentacaoInternaEmElaboracaoInputData::getIdMovimentacao, Objects::nonNull, "O Id é nulo")
            .get();
    }

    private Movimentacao buscarMovimentacao(GerarMemorandoMovimentacaoInternaEmElaboracaoInputData inputData) {
        Optional<Movimentacao> movimentacao = movimentacaoDataProvider.buscarPorId(inputData.getIdMovimentacao());
        return movimentacao.orElseThrow(MovimentacaoNaoEncontradaException::new);
    }

    private void validarSeMovimentacaoEmElaboracao(Movimentacao movimentacao) {
        if (movimentacao.getSituacao().equals(Movimentacao.Situacao.FINALIZADO)) {
            throw new GerarMemorandoMovimentacaoEmElaboracaoException();
        }
    }

    private MemorandoMovimentacaoInterna montarMemorando(Movimentacao movimentacao){

        MemorandoMovimentacaoInterna memorandoMovimentacaoInterna = new MemorandoMovimentacaoInterna();

        memorandoMovimentacaoInterna.setNumeroMemorando(gerarNumeroMemorando(movimentacao)+"/"+LocalDateTime.now().getYear());
        memorandoMovimentacaoInterna.setOrgao(formatarOrgao(movimentacao));
        memorandoMovimentacaoInterna.setSetorDestino(formatarSetorDestino(movimentacao));
        memorandoMovimentacaoInterna.setSetorOrigem(formatarSetorOrigem(movimentacao));
        memorandoMovimentacaoInterna.setNumeroMovimentacao(movimentacao.getCodigo());
        memorandoMovimentacaoInterna.setMotivoObs(movimentacao.getMotivoObservacao());
        memorandoMovimentacaoInterna.setTipo(memorandoMovimentacaoInternaUtils.formatarTituloMemorando(movimentacao.getTipo()));
        memorandoMovimentacaoInterna.setNumeroProcesso(movimentacao.getNumeroProcesso());
        if (movimentacao.getTipo().equals(TipoMovimentacaoEnum.TEMPORARIA)) {
            memorandoMovimentacaoInterna.setTemporaria(Boolean.TRUE);
            memorandoMovimentacaoInterna.setDataEsperadaDevolucao(memorandoMovimentacaoInternaUtils.formatarEsperadaDeDevolucao(movimentacao));
            if (movimentacao.getSituacao().equals(Movimentacao.Situacao.EM_ELABORACAO)) {
                memorandoMovimentacaoInterna.setEmElaboracao(Boolean.TRUE);
            }else {
                memorandoMovimentacaoInterna.setEmElaboracao(Boolean.FALSE);
                memorandoMovimentacaoInterna.setDataEnvio(memorandoMovimentacaoInternaUtils.formatarDataEnvioMovimentacao(movimentacao));
                memorandoMovimentacaoInterna.setDataFinalizacao(memorandoMovimentacaoInternaUtils.formatarDataFinalizacaoMovimentacao(movimentacao));
            }

        }else {
            memorandoMovimentacaoInterna.setEmElaboracao(Boolean.TRUE);
            memorandoMovimentacaoInterna.setTemporaria(Boolean.FALSE);
        }

        if (movimentacaoItemDataProvider.existePorMovimentacaoId(movimentacao.getId())) {
            List<Patrimonio> patrimonios = encontrarPatrimoniosPorMovimentacao(movimentacao);
            List<MemorandoMovimentacaoInterna.ContaContabil> itensContasContabeis = prepararContasContabeisParaMemorando(patrimonios, movimentacao);
            atualizarTotalizadoresPorContaContabil(itensContasContabeis);

            memorandoMovimentacaoInterna.setValorEntrada(memorandoMovimentacaoInternaUtils.somarValorEntradaTotalPorOrgao(itensContasContabeis));
            memorandoMovimentacaoInterna.setValorDepreciadoAcumulado(memorandoMovimentacaoInternaUtils.somarDepreciadoAcumuladoTotalPorOrgao(itensContasContabeis));
            memorandoMovimentacaoInterna.setValorLiquido(memorandoMovimentacaoInternaUtils.somarValorLiquidoTotalPorOrgao(itensContasContabeis));
            memorandoMovimentacaoInterna.setContasContabeis(itensContasContabeis);
            memorandoMovimentacaoInterna.setPossuiItens(Boolean.TRUE);

            memorandoMovimentacaoInterna.getContasContabeis().sort(Comparator.comparing(MemorandoMovimentacaoInterna.ContaContabil::getDescricao));
        }else {
            memorandoMovimentacaoInterna.setPossuiItens(Boolean.FALSE);
        }

        return memorandoMovimentacaoInterna;
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

    List<MemorandoMovimentacaoInterna.ContaContabil> prepararContasContabeisParaMemorando(List<Patrimonio> patrimonios, Movimentacao movimentacao) {
        List<ContaContabil> contasContabeis =  encontrarContasContabeis(patrimonios);
        List<MemorandoMovimentacaoInterna.ContaContabil> contasContabeisMemorando = new ArrayList<>();

        for (ContaContabil contaContabil: contasContabeis) {
            contasContabeisMemorando.add(
                MemorandoMovimentacaoInterna.ContaContabil
                    .builder()
                    .id(contaContabil.getId())
                    .descricao(memorandoMovimentacaoInternaUtils.formatarContaContabil(contaContabil))
                    .patrimonios(converterPatrimoniosParaRelatorio(filtrarPatrimoniosPorContaContabil(patrimonios, contaContabil), movimentacao))
                    .build());
        }
        return contasContabeisMemorando;
    }

    private List<MemorandoMovimentacaoInterna.Patrimonio> converterPatrimoniosParaRelatorio(List<Patrimonio> patrimonios, Movimentacao movimentacao) {
        List<MemorandoMovimentacaoInterna.Patrimonio> registrosPatrimonios = new ArrayList<>();
        for (Patrimonio patrimonio: patrimonios) {
            registrosPatrimonios.add(
                MemorandoMovimentacaoInterna.Patrimonio
                    .builder()
                    .id(patrimonio.getId())
                    .numero(patrimonio.getNumero())
                    .descricao(patrimonio.getItemIncorporacao().getDescricao())
                    .valorEntrada(patrimonio.getValorEntrada())
                    .valorDepreciadoAcumulado(patrimonio.getValorEntrada().subtract(patrimonio.getValorLiquido()))
                    .valorLiquido(patrimonio.getValorLiquido())
                    .devolvidoNaData(buscarDataDevolucaoNaMovimentacao(patrimonio, movimentacao))
                    .build()
            );
        }
        return registrosPatrimonios;
    }

    private String buscarDataDevolucaoNaMovimentacao(Patrimonio patrimonio, Movimentacao movimentacao) {
       Optional<MovimentacaoItem> itemMovimentacao = movimentacaoItemDataProvider.buscarPorMovimentacaoEPatrimonio(movimentacao.getId(), patrimonio.getId());
       LocalDateTime dataDevolucao = null;
       if (itemMovimentacao.isPresent()) {
           dataDevolucao = itemMovimentacao.get().getDataDevolucao();
       }
       if (Objects.nonNull(dataDevolucao)) {
           DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
           return dataDevolucao.format(formatter);
       }
       return null;

    }

    private List<ContaContabil> encontrarContasContabeis(List<Patrimonio> patrimonios) {
        List<ContaContabil> contasContabeis = new ArrayList<>();
        for (Patrimonio patrimonio: patrimonios) {
            if (!contasContabeis.contains(patrimonio.getContaContabilAtual())){
                contasContabeis.add(patrimonio.getContaContabilAtual());
            }
        }
        return contasContabeis;
    }

    private List<Patrimonio> filtrarPatrimoniosPorContaContabil(List<Patrimonio> patrimonios, ContaContabil contaContabil) {
        List<Patrimonio> patrimoniosNaContaContabil = new ArrayList<>();
        for (Patrimonio patrimonio: patrimonios) {
            if (patrimonio.getContaContabilAtual().getId().equals(contaContabil.getId()) && !patrimoniosNaContaContabil.contains(patrimonio)) {
                patrimoniosNaContaContabil.add(patrimonio);
            }
        }
        return patrimoniosNaContaContabil;
    }

    private String formatarOrgao(Movimentacao movimentacao) {
        if (Objects.nonNull(movimentacao.getOrgaoOrigem())) {
            return movimentacao.getOrgaoOrigem().getSigla()+" - "+movimentacao.getOrgaoOrigem().getNome();
        }
        return SEM_VALOR;
    }

    private String formatarSetorOrigem(Movimentacao movimentacao) {
        if (Objects.nonNull(movimentacao.getSetorOrigem())) {
            return movimentacao.getSetorOrigem().getSigla()+" - "+movimentacao.getSetorOrigem().getNome();
        }
        return SEM_VALOR;
    }

    private String formatarSetorDestino(Movimentacao movimentacao) {
        if (Objects.nonNull(movimentacao.getSetorDestino())) {
            return movimentacao.getSetorDestino().getSigla()+" - "+movimentacao.getSetorDestino().getNome();
        }
        return SEM_VALOR;
    }


    private List<Patrimonio> encontrarPatrimoniosPorMovimentacao(Movimentacao movimentacao) {
        List<MovimentacaoItem> itensMovimentacao = movimentacaoItemDataProvider.buscarPorMovimentacaoId(movimentacao.getId());

        return itensMovimentacao.stream()
            .map(MovimentacaoItem::getPatrimonio)
            .collect(Collectors.toList());
    }


    private String gerarNumeroMemorando(Movimentacao movimentacao) {
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

}
