package br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.calculardepreciacao;

import br.com.azi.patrimoniomobiliario.domain.constant.movimentacao.TipoMovimentacaoEnum;
import br.com.azi.patrimoniomobiliario.domain.entity.Depreciacao;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.MovimentacaoItem;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.entity.PeriodoDepreciacao;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.DepreciacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.calculardepreciacao.converter.CalcularDepreciacaoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.calculardepreciacao.exception.DistribuicaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.calculardepreciacao.exception.PatrimonioNaoEncontradoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.calculardepreciacao.exception.UltimaDepreciacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.calculardepreciacao.exception.UltimaMovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.utils.date.DateUtils;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@AllArgsConstructor
public class CalcularDepreciacaoUseCaseImpl implements CalcularDepreciacaoUseCase {

    private final Clock clock;

    private final PatrimonioDataProvider patrimonioDataProvider;

    private final DepreciacaoDataProvider depreciacaoDataProvider;

    private final MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    private final boolean depreciaEmAlmoxarifado;

    private final CalcularDepreciacaoOutputDataConverter calcularDepreciacaoOutputDataConverter;

    @Override
    public CalcularDepreciacaoOutputData executar(CalcularDepreciacaoInputData inputData) {
        final Patrimonio patrimonio = buscarPatrimonio(inputData);

        final PeriodoDepreciacao periodo = calcularPeriodoDepreciacao(patrimonio);
        final List<Depreciacao> depreciacoes = calcularDepreciacoesPatrimonio(patrimonio, periodo);
        atualizarValorLiquidoPatrimonio(patrimonio, depreciacoes);

        return calcularDepreciacaoOutputDataConverter.to(patrimonio, depreciacoes);
    }

    private Patrimonio buscarPatrimonio(CalcularDepreciacaoInputData inputData) {
        return patrimonioDataProvider.buscarPorId(inputData.getPatrimonio())
            .orElseThrow(() -> new PatrimonioNaoEncontradoException(inputData.getPatrimonio()));
    }

    private PeriodoDepreciacao calcularPeriodoDepreciacao(Patrimonio patrimonio) {
        final PeriodoDepreciacao periodo = new PeriodoDepreciacao();
        if (unicaDepreciacao(patrimonio)) {
            periodo.setDataInicial(patrimonio.getInicioVidaUtil().toLocalDate());
            periodo.setDataFinal(patrimonio.getFimVidaUtil().toLocalDate());
        } else {
            periodo.setDataFinal(YearMonth.from(LocalDateTime.now(clock).minusMonths(1)).atEndOfMonth());
            if (primeiraDepreciacao(patrimonio)) {
                periodo.setDataInicial(patrimonio.getInicioVidaUtil().toLocalDate());
            } else {
                Depreciacao depreciacao = buscaUltimaDepreciacao(patrimonio);
                periodo.setDataInicial(YearMonth.from(depreciacao.getDataInicial().plusMonths(1)).atDay(1));
            }
        }
        return periodo;
    }

    private boolean unicaDepreciacao(Patrimonio patrimonio) {
        return primeiraDepreciacao(patrimonio) && patrimonio.getFimVidaUtil().isBefore(LocalDateTime.now(clock));
    }

    private boolean primeiraDepreciacao(Patrimonio patrimonio) {
        return !depreciacaoDataProvider.existePorPatrimonio(patrimonio.getId());
    }

    private Depreciacao buscaUltimaDepreciacao(Patrimonio patrimonio) {
        return depreciacaoDataProvider.buscarUltimaPorPatrimonio(patrimonio.getId())
            .orElseThrow(() -> new UltimaDepreciacaoNaoEncontradaException(patrimonio.getId()));
    }

    private List<Depreciacao> calcularDepreciacoesPatrimonio(Patrimonio patrimonio, PeriodoDepreciacao periodo) {
        if (verificaSePatrimonioAtingiuValorResidual(patrimonio) || verificaPeriodoInvalido(periodo)) {
            return Collections.emptyList();
        }

        if (verificaPeriodoAcumulado(periodo)) {
            return gerarDepreciacaoPeriodoAcumulado(patrimonio, periodo);
        } else if (verificaDepreciacaoPendenteNoPeriodo(patrimonio, periodo)) {
            return Collections.singletonList(gerarDepreciacao(patrimonio,
                periodo.getDataInicial().atStartOfDay(),
                periodo.getDataFinal().atTime(23, 59, 59))
            );
        } else {
            return Collections.emptyList();
        }
    }

    private boolean verificaSePatrimonioAtingiuValorResidual(Patrimonio patrimonio) {
        final BigDecimal valorResidual = patrimonio.getValorResidual();
        final BigDecimal valorLiquido = patrimonio.getValorLiquido();
        return valorResidual.compareTo(valorLiquido) >= 0;
    }

    private boolean verificaPeriodoInvalido(PeriodoDepreciacao periodo) {
        return periodo.getDataFinal().isBefore(periodo.getDataInicial());
    }

    private boolean verificaPeriodoAcumulado(PeriodoDepreciacao periodo) {
        return DateUtils.totalMeses(periodo.getDataInicial().withDayOfMonth(1), YearMonth.from(periodo.getDataFinal()).atEndOfMonth()) > 1;
    }

    private List<Depreciacao> gerarDepreciacaoPeriodoAcumulado(Patrimonio patrimonio, PeriodoDepreciacao periodo) {
        final List<Depreciacao> depreciacoes = new ArrayList<>();
        final List<PeriodoDepreciacao> periodos = separaPeriodoEmMeses(periodo);

        for (PeriodoDepreciacao mes : periodos) {
            if (verificaDepreciacaoPendenteNoPeriodo(patrimonio, mes)) {
                depreciacoes.add(gerarDepreciacao(patrimonio,
                    mes.getDataInicial().atStartOfDay(),
                    mes.getDataFinal().atTime(23, 59, 59)));
            }
        }

        return depreciacoes;
    }

    private List<PeriodoDepreciacao> separaPeriodoEmMeses(PeriodoDepreciacao periodo) {
        final int meses = DateUtils.totalMeses(periodo.getDataInicial().withDayOfMonth(1),
            YearMonth.from(periodo.getDataFinal()).atEndOfMonth());

        final List<PeriodoDepreciacao> intervalos = new ArrayList<>();

        intervalos.add(criarPeriodo(periodo.getDataInicial(), YearMonth.from(periodo.getDataInicial()).atEndOfMonth()));
        if (meses > 2) {
            for (int i = 2; i < meses; i++) {
                LocalDate mesCorrente = periodo.getDataInicial().plusMonths((long) i - 1);
                intervalos.add(criarPeriodo(LocalDate.now(clock).withDayOfMonth(1).withMonth(mesCorrente.getMonth().getValue()).withYear(mesCorrente.getYear()),
                    YearMonth.from(LocalDate.now(clock).withDayOfMonth(1).withMonth(mesCorrente.getMonth().getValue()).withYear(mesCorrente.getYear())).atEndOfMonth()));
            }
        }
        intervalos.add(criarPeriodo(periodo.getDataFinal().withDayOfMonth(1), periodo.getDataFinal()));

        return intervalos;
    }

    private PeriodoDepreciacao criarPeriodo(LocalDate inicio, LocalDate fim) {
        return PeriodoDepreciacao
            .builder()
            .dataInicial(inicio)
            .dataFinal(fim)
            .build();
    }

    private Depreciacao gerarDepreciacao(Patrimonio patrimonio, LocalDateTime dataInicio, LocalDateTime dataFinal) {
        final Movimentacao ultimaMovimentacao = buscarUltimaMovimentacaoPatrimonio(patrimonio.getId(), dataFinal);
        final BigDecimal valorAnterior = patrimonio.getValorLiquido();
        final BigDecimal valorSubtraido = calcularValorDepreciadoNoPeriodo(patrimonio, dataInicio, dataFinal, ultimaMovimentacao);
        final BigDecimal valorPosterior = subtrairValorDepreciado(patrimonio.getValorLiquido(), valorSubtraido);
        final BigDecimal taxaAplicada = calcularTaxaAplicada(valorSubtraido, patrimonio);

        if (verificarUltimoMesVidaUtil(patrimonio, dataFinal)) {
            dataFinal = patrimonio.getFimVidaUtil();
        }

        patrimonio.setValorLiquido(valorPosterior);

        return Depreciacao.builder()
            .valorAnterior(valorAnterior)
            .valorPosterior(valorPosterior)
            .valorSubtraido(valorSubtraido)
            .taxaAplicada(taxaAplicada)
            .dataInicial(dataInicio)
            .dataFinal(dataFinal)
            .patrimonio(patrimonio)
            .orgao(ultimaMovimentacao.getOrgaoDestino())
            .setor(ultimaMovimentacao.getSetorDestino())
            .contaContabil(patrimonio.getContaContabilAtual())
            .mesReferencia(DateUtils.formatarData(dataInicio, "MM/yyyy"))
            .build();
    }

    private Movimentacao buscarUltimaMovimentacaoPatrimonio(Long patrimonioId, LocalDateTime dataFinal) {
        return movimentacaoItemDataProvider
            .buscarUltimaMovimentacaoPatrimonioAntesDe(patrimonioId, DateUtils.asDate(dataFinal))
            .map(MovimentacaoItem::getMovimentacao)
            .orElseThrow(() -> new UltimaMovimentacaoNaoEncontradaException(patrimonioId, dataFinal));
    }

    private boolean verificarUltimoMesVidaUtil(Patrimonio patrimonio, LocalDateTime dataFim) {
        return patrimonio.getFimVidaUtil().getMonth() == dataFim.getMonth()
            && patrimonio.getFimVidaUtil().getYear() == dataFim.getYear();
    }

    private BigDecimal calcularValorDepreciadoNoPeriodo(Patrimonio patrimonio, LocalDateTime dataInicial, LocalDateTime dataFinal, Movimentacao ultimaMovimentacao) {
        if (verificarUltimoMesVidaUtil(patrimonio, dataFinal)) {
            return calcularValorDepreciacaoRestante(patrimonio);
        }

        if (depreciaEmAlmoxarifado) {
            if (verificaDepreciacaoDeMesCheio(dataInicial, dataFinal)) {
                return patrimonio.getValorDepreciacaoMensal();
            } else {
                return calcularValorDepreciacaoParcial(patrimonio, DateUtils.totalDias(dataInicial, dataFinal));
            }
        } else {
            return calcularValorDepreciadoNoPeriodoDeUso(patrimonio, dataInicial, dataFinal, ultimaMovimentacao);
        }
    }

    private BigDecimal calcularValorDepreciacaoRestante(Patrimonio patrimonio) {
        return patrimonio.getValorLiquido().subtract(patrimonio.getValorResidual());
    }

    private boolean verificaDepreciacaoDeMesCheio(LocalDateTime dataInicial, LocalDateTime dataFinal) {
        return DateUtils.mesCheio(dataInicial, dataFinal);
    }

    private BigDecimal calcularValorDepreciacaoParcial(Patrimonio patrimonio, int totalDias) {
        BigDecimal valorDepreciacaoDiario = calcularValorDepreciacaoDiario(patrimonio.getValorDepreciacaoMensal());
        return valorDepreciacaoDiario.multiply(BigDecimal.valueOf(totalDias),
            new MathContext(10)).setScale(2, RoundingMode.HALF_EVEN);
    }

    private BigDecimal calcularValorDepreciacaoDiario(BigDecimal valorDepreciacaoMensal) {
        return valorDepreciacaoMensal.divide(BigDecimal.valueOf(30), new MathContext(10)).setScale(2, RoundingMode.HALF_EVEN);
    }

    private BigDecimal calcularValorDepreciadoNoPeriodoDeUso(Patrimonio patrimonio, LocalDateTime dataInicial, LocalDateTime dataFinal, Movimentacao ultimaMovimentacao) {
        if (verificaPatrimonioNoAlmoxarifado(ultimaMovimentacao)) {
            return BigDecimal.ZERO;
        } else if (verificaSeHouveDistribuicaoNoPeriodo(patrimonio, dataInicial, dataFinal)) {
            return calcularValorDepreciacaoParcialPorUltimaDistribuicao(patrimonio, dataInicial, dataFinal);
        } else {
            return patrimonio.getValorDepreciacaoMensal();
        }
    }

    private boolean verificaPatrimonioNoAlmoxarifado(Movimentacao ultimaMovimentacao) {
        return TipoMovimentacaoEnum.DEVOLUCAO_ALMOXARIFADO.equals(ultimaMovimentacao.getTipo())
            || TipoMovimentacaoEnum.ENTRE_ESTOQUES.equals(ultimaMovimentacao.getTipo());
    }

    private boolean verificaSeHouveDistribuicaoNoPeriodo(Patrimonio patrimonio, LocalDateTime dataInicial, LocalDateTime dataFinal) {
        return movimentacaoItemDataProvider.existeDistribuicaoFinalizadaParaPatrimonioNoPeriodo(patrimonio.getId(),
            DateUtils.asDate(dataInicial), DateUtils.asDate(dataFinal));
    }

    private BigDecimal calcularValorDepreciacaoParcialPorUltimaDistribuicao(Patrimonio patrimonio, LocalDateTime dataInicial, LocalDateTime dataFinal) {
        final Movimentacao ultimaDistribuicaoPeriodo = buscarDistribuicaoNoPeriodo(patrimonio, dataInicial, dataFinal);
        final LocalDateTime dataDistribuicao = ultimaDistribuicaoPeriodo.getDataFinalizacao()
            .withHour(0).withMinute(0).withSecond(0);

        if (verificaDepreciacaoDeMesCheio(dataDistribuicao, dataFinal)) {
            return patrimonio.getValorDepreciacaoMensal();
        } else {
            final int diasEmUso = DateUtils.totalDias(dataDistribuicao, dataFinal);
            return calcularValorDepreciacaoParcial(patrimonio, diasEmUso);
        }
    }

    private Movimentacao buscarDistribuicaoNoPeriodo(Patrimonio patrimonio, LocalDateTime dataInicial, LocalDateTime dataFinal) {
        return movimentacaoItemDataProvider
            .buscarUltimaMovimentacaoPatrimonioPorTipoNoPeriodo(
                patrimonio.getId(),
                TipoMovimentacaoEnum.DISTRIBUICAO,
                DateUtils.asDate(dataInicial),
                DateUtils.asDate(dataFinal)
            )
            .map(MovimentacaoItem::getMovimentacao)
            .orElseThrow(() -> new DistribuicaoNaoEncontradaException(patrimonio.getId(), dataInicial, dataFinal));
    }

    private BigDecimal subtrairValorDepreciado(BigDecimal valorLiquido, BigDecimal valorDepreciacao) {
        return valorLiquido.subtract(valorDepreciacao, new MathContext(10)).setScale(2, RoundingMode.HALF_EVEN);
    }

    private BigDecimal calcularTaxaAplicada(BigDecimal valorDepreciado, Patrimonio patrimonio) {
        BigDecimal valorDepreciavel = calcularValorDepreciavel(patrimonio.getValorAquisicao(), patrimonio.getValorResidual());
        return valorDepreciado.multiply(BigDecimal.valueOf(100), new MathContext(10)).divide(valorDepreciavel, new MathContext(10)).setScale(2, RoundingMode.HALF_EVEN);
    }

    private BigDecimal calcularValorDepreciavel(BigDecimal valorAquisicao, BigDecimal valorResidual) {
        return valorAquisicao.subtract(valorResidual);
    }

    private boolean verificaDepreciacaoPendenteNoPeriodo(Patrimonio patrimonio, PeriodoDepreciacao periodo) {
        return !depreciacaoDataProvider.existePorPatrimonioNoPeriodo(
            patrimonio.getId(),
            periodo.getDataInicial().atStartOfDay()
        );
    }

    private void atualizarValorLiquidoPatrimonio(Patrimonio patrimonio, List<Depreciacao> depreciacoes) {
        if (!depreciacoes.isEmpty()) {
            Depreciacao mininoPorValorPosterior = depreciacoes
                .stream()
                .min(Comparator.comparing(Depreciacao::getValorPosterior))
                .orElseThrow(NoSuchElementException::new);

            patrimonio.setValorLiquido(mininoPorValorPosterior.getValorPosterior());
        }
    }
}

