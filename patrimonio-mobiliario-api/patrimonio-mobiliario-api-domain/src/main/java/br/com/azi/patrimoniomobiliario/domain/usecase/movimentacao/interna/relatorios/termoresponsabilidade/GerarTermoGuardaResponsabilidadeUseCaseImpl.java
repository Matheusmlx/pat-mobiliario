package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.relatorios.termoresponsabilidade;

import br.com.azi.patrimoniomobiliario.domain.constant.movimentacao.TipoMovimentacaoEnum;
import br.com.azi.patrimoniomobiliario.domain.entity.Arquivo;
import br.com.azi.patrimoniomobiliario.domain.entity.LancamentoContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.MovimentacaoItem;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.entity.TermoGuardaResposabilidadeMovimentacaoInterna;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.LancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeRelatoriosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.relatorios.memorando.memorandomovimentacaofinalizada.exception.LancamentoContabilPorPatrimonioException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.relatorios.termoresponsabilidade.converter.GerarTermoGuardaResponsabilidadeOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.relatorios.termoresponsabilidade.exception.GerarTermoResponsabilidadeException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.relatorios.termoresponsabilidade.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.utils.string.StringUtils;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
public class GerarTermoGuardaResponsabilidadeUseCaseImpl implements GerarTermoGuardaResponsabilidadeUseCase {

    private final MovimentacaoDataProvider movimentacaoDataProvider;

    private final MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    private final LancamentoContabilDataProvider lancamentoContabilDataProvider;

    private final SistemaDeRelatoriosIntegration sistemaDeRelatoriosIntegration;

    private final GerarTermoGuardaResponsabilidadeOutputDataConverter outputDataConverter;

    @Override
    public GerarTermoGuardaResponsabilidadeOutputData executar(GerarTermoGuardaResponsabilidadeInputData inputData) {
        validarDadosEntrada(inputData);
        Movimentacao movimentacao = buscarMovimentacao(inputData);
        validarSeMovimentacaoFinalizada(movimentacao);


        List<Long> patrimoniosId = buscarPatrimoiosDaMovimentacao(movimentacao);

        List<LancamentoContabil>  lancamentosContabeis = buscarLancamentosContabeisDePatrimoniosPorMovimentacao( patrimoniosId, movimentacao);

        Arquivo arquivo = sistemaDeRelatoriosIntegration.gerarTermoGuardaResponsabilidadeMovimentacaoInterna(montarTermoResponsabilidade(movimentacao, lancamentosContabeis));
        return outputDataConverter.to(arquivo);
    }

    private void validarDadosEntrada(GerarTermoGuardaResponsabilidadeInputData inputData) {
        Validator.of(inputData)
            .validate(GerarTermoGuardaResponsabilidadeInputData::getIdMovimentacao, Objects::nonNull, "O Id é nulo")
            .get();
    }

    private Movimentacao buscarMovimentacao(GerarTermoGuardaResponsabilidadeInputData inputData) {
        Optional<Movimentacao> movimentacao = movimentacaoDataProvider.buscarPorId(inputData.getIdMovimentacao());
        return movimentacao.orElseThrow(MovimentacaoNaoEncontradaException::new);
    }

    private void validarSeMovimentacaoFinalizada(Movimentacao movimentacao) {
        if (movimentacao.getSituacao().equals(Movimentacao.Situacao.EM_ELABORACAO)) {
            throw new GerarTermoResponsabilidadeException("Não é possível gerar termo para movimentações não finalizadas.");
        }
    }

    private List<Long> buscarPatrimoiosDaMovimentacao(Movimentacao movimentacao) {
        List<MovimentacaoItem> itens = movimentacaoItemDataProvider.buscarPorMovimentacaoId(movimentacao.getId());
        return itens.stream()
            .map(item -> item.getPatrimonio().getId())
            .collect(Collectors.toList());
    }

    private List<LancamentoContabil>  buscarLancamentosContabeisDePatrimoniosPorMovimentacao(List<Long> patrimonios, Movimentacao movimentacao) {
        return lancamentoContabilDataProvider.buscarCreditoPorPatrimoniosEMovimentacao(patrimonios, movimentacao.getId());
    }

    private TermoGuardaResposabilidadeMovimentacaoInterna montarTermoResponsabilidade(Movimentacao movimentacao, List<LancamentoContabil> lancamentoContabeis){
        List<TermoGuardaResposabilidadeMovimentacaoInterna.Patrimonio> patrimonios = encontrarPatrimoniosPorMovimentacao(movimentacao, lancamentoContabeis);

       return TermoGuardaResposabilidadeMovimentacaoInterna .builder()
            .numeroTermo(gerarNumeroTermoResponsabilidade(movimentacao)+"/"+buscarAnoDaMovimentacao(movimentacao))
            .orgao(configurarUnidadeOrganizacional(movimentacao.getOrgaoOrigem()))
            .setor(configurarUnidadeOrganizacional(movimentacao.getSetorDestino()))
            .tipo(movimentacao.getTipo().getValor())
            .dataMovimentacao(formatarDataMovimentacao(movimentacao))
            .motivoObs(movimentacao.getMotivoObservacao())
            .totalDeBens(patrimonios.size())
            .responsavel(formatarResponsavel(movimentacao))
            .valorTotal(calcularValorTotalMovimentacao(patrimonios))
            .patrimonios(patrimonios)
            .build();
    }

    private String configurarUnidadeOrganizacional(UnidadeOrganizacional unidadeOrganizacional) {
        return unidadeOrganizacional.getSigla()+" - "+unidadeOrganizacional.getNome();
    }

    private BigDecimal calcularValorTotalMovimentacao(List<TermoGuardaResposabilidadeMovimentacaoInterna.Patrimonio> patrimonios) {
        return patrimonios.stream().map(TermoGuardaResposabilidadeMovimentacaoInterna.Patrimonio::getValorLiquido)
            .reduce(BigDecimal.ZERO,BigDecimal::add);
    }

    private String formatarResponsavel(Movimentacao movimentacao){
        if(Objects.nonNull(movimentacao.getResponsavel())){
            return movimentacao.getResponsavel().getNome();
        }
        return "-";
    }

    private String formatarDataMovimentacao(Movimentacao movimentacao) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        if (movimentacao.getTipo().equals(TipoMovimentacaoEnum.TEMPORARIA)){
            return movimentacao.getDataEnvio().format(formatter);
        }
        return movimentacao.getDataFinalizacao().format(formatter);
    }

    private LancamentoContabil filtrarLancamentoContabilParaPatrimonio(List<LancamentoContabil> lcs, Patrimonio patrimonio) {
        return lcs
            .stream()
            .filter(map -> patrimonio.getId().equals(map.getPatrimonio().getId()))
            .findFirst()
            .orElseThrow(LancamentoContabilPorPatrimonioException::new);
    }

    private List<TermoGuardaResposabilidadeMovimentacaoInterna.Patrimonio> converterPatrimoniosParaRelatorio(List<Patrimonio> patrimonios, List<LancamentoContabil> lancamentosContabeis) {
        List<TermoGuardaResposabilidadeMovimentacaoInterna.Patrimonio> registrosPatrimonios = new ArrayList<>();
        for (Patrimonio patrimonio: patrimonios) {
            LancamentoContabil lancamentoContabil = filtrarLancamentoContabilParaPatrimonio(lancamentosContabeis, patrimonio);
            registrosPatrimonios.add(
                TermoGuardaResposabilidadeMovimentacaoInterna.Patrimonio
                    .builder()
                    .id(patrimonio.getId())
                    .numero(patrimonio.getNumero())
                    .descricao(patrimonio.getItemIncorporacao().getDescricao())
                    .valorLiquido(lancamentoContabil.getValor())
                    .build()
            );
        }
        return registrosPatrimonios;
    }

    private List<TermoGuardaResposabilidadeMovimentacaoInterna.Patrimonio> encontrarPatrimoniosPorMovimentacao(Movimentacao movimentacao, List<LancamentoContabil> lancamentoContabeis) {
        List<MovimentacaoItem> itensMovimentacao = movimentacaoItemDataProvider.buscarPorMovimentacaoId(movimentacao.getId());
        List<Patrimonio> patrimonios = itensMovimentacao.stream()
            .map(MovimentacaoItem::getPatrimonio)
            .collect(Collectors.toList());

        return converterPatrimoniosParaRelatorio(patrimonios, lancamentoContabeis);
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
        if (movimentacao.getTipo().equals(TipoMovimentacaoEnum.TEMPORARIA)){
            return String.valueOf(movimentacao.getDataEnvio().getYear());
        }
        return String.valueOf(movimentacao.getDataFinalizacao().getYear());
    }
}
