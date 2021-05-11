package br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.calculardepreciacao;

import br.com.azi.patrimoniomobiliario.domain.constant.movimentacao.TipoMovimentacaoEnum;
import br.com.azi.patrimoniomobiliario.domain.entity.Depreciacao;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.MovimentacaoItem;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.entity.PeriodoDepreciacao;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.DepreciacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.calculardepreciacao.converter.CalcularDepreciacaoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.utils.date.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CalcularDepreciacaoAcumuladaTest {

    private static final LocalDate LOCAL_DATE = LocalDate.of(2020, 7, 1);
    private static final boolean DEPRECIA_EM_ALMOXARIFADO = true;

    @Mock
    private PatrimonioDataProvider patrimonioDataProvider;

    @Mock
    private DepreciacaoDataProvider depreciacaoDataProvider;

    @Mock
    private MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    @InjectMocks
    private CalcularDepreciacaoOutputDataConverter calcularDepreciacaoOutputDataConverter;

    private CalcularDepreciacaoUseCaseImpl usecase;

    @Before
    public void instanciarUseCase() {
        final Clock fixedClock = Clock.fixed(LOCAL_DATE.atStartOfDay(
            ZoneId.systemDefault()).toInstant(),
            ZoneId.systemDefault()
        );

        usecase = new CalcularDepreciacaoUseCaseImpl(
            fixedClock,
            patrimonioDataProvider,
            depreciacaoDataProvider,
            movimentacaoItemDataProvider,
            DEPRECIA_EM_ALMOXARIFADO,
            calcularDepreciacaoOutputDataConverter);
    }

    @Test
    public void deveCalcularDepreciacaoAcumuladaDesdePrimeiraDistribuicao() {
        UnidadeOrganizacional orgao = UnidadeOrganizacional.builder().id(1L).build();
        UnidadeOrganizacional setor = UnidadeOrganizacional.builder().id(2L).build();

        Patrimonio patrimonio = Patrimonio.builder()
            .id(1L)
            .valorLiquido(BigDecimal.valueOf(50000))
            .valorEntrada(BigDecimal.valueOf(50000))
            .valorAquisicao(BigDecimal.valueOf(50000))
            .valorResidual(BigDecimal.valueOf(5000))
            .valorDepreciacaoMensal(BigDecimal.valueOf(375))
            .fimVidaUtil(LocalDateTime.of(2030, 3, 24, 23, 59, 59))
            .inicioVidaUtil(LocalDateTime.of(2020, 3, 15, 0, 0, 0))
            .orgao(orgao)
            .setor(setor)
            .build();

        Patrimonio patrimonioDepreciado = Patrimonio.builder()
            .id(1L)
            .valorLiquido(BigDecimal.valueOf(48662.5).setScale(2, RoundingMode.HALF_EVEN))
            .valorEntrada(BigDecimal.valueOf(50000))
            .valorAquisicao(BigDecimal.valueOf(50000))
            .valorResidual(BigDecimal.valueOf(5000))
            .valorDepreciacaoMensal(BigDecimal.valueOf(375))
            .fimVidaUtil(LocalDateTime.of(2030, 3, 24, 23, 59, 59))
            .inicioVidaUtil(LocalDateTime.of(2020, 3, 15, 0, 0, 0))
            .orgao(orgao)
            .setor(setor)
            .build();

        Movimentacao ultimaMovimentacao = Movimentacao.builder()
            .id(1L)
            .tipo(TipoMovimentacaoEnum.DISTRIBUICAO)
            .dataFinalizacao(LocalDateTime.of(2020, 3, 15, 0, 0, 0))
            .orgaoDestino(orgao)
            .setorDestino(setor)
            .build();

        Depreciacao depreciacaoMarco = Depreciacao.builder()
            .dataInicial(LocalDateTime.of(2020, 3, 15, 0, 0, 0))
            .dataFinal(LocalDateTime.of(2020, 3, 31, 23, 59, 59))
            .mesReferencia("03/2020")
            .valorAnterior(BigDecimal.valueOf(50000))
            .valorPosterior(new BigDecimal("49787.50").setScale(2, RoundingMode.HALF_EVEN))
            .valorSubtraido(new BigDecimal("212.50").setScale(2, RoundingMode.HALF_EVEN))
            .taxaAplicada(new BigDecimal("0.47").setScale(2, RoundingMode.HALF_EVEN))
            .patrimonio(patrimonio)
            .orgao(orgao)
            .setor(setor)
            .build();

        PeriodoDepreciacao periodoMarco = criarPeriodoDepreciacao(15, 31, 3);
        PeriodoDepreciacao periodoAbril = criarPeriodoDepreciacao(1, 30, 4);
        PeriodoDepreciacao periodoMaio = criarPeriodoDepreciacao(1, 31, 5);
        PeriodoDepreciacao periodoJunho = criarPeriodoDepreciacao(1, 30, 6);

        Depreciacao depreciacaoAbril = criarDepreciacao(
            LocalDateTime.of(periodoAbril.getDataInicial(), LocalTime.of(0, 0, 0)),
            LocalDateTime.of(periodoAbril.getDataFinal(), LocalTime.of(23, 59, 59)),
            depreciacaoMarco,
            BigDecimal.valueOf(0.83).setScale(2, RoundingMode.HALF_EVEN)
        );
        Depreciacao depreciacaoMaio = criarDepreciacao(
            LocalDateTime.of(periodoMaio.getDataInicial(), LocalTime.of(0, 0, 0)),
            LocalDateTime.of(periodoMaio.getDataFinal(), LocalTime.of(23, 59, 59)),
            depreciacaoAbril,
            BigDecimal.valueOf(0.83).setScale(2, RoundingMode.HALF_EVEN)
        );
        Depreciacao depreciacaoJunho = criarDepreciacao(
            LocalDateTime.of(periodoJunho.getDataInicial(), LocalTime.of(0, 0, 0)),
            LocalDateTime.of(periodoJunho.getDataFinal(), LocalTime.of(23, 59, 59)),
            depreciacaoMaio,
            BigDecimal.valueOf(0.83).setScale(2, RoundingMode.HALF_EVEN)
        );

        when(patrimonioDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(patrimonio));

        when(depreciacaoDataProvider.existePorPatrimonio(anyLong())).thenReturn(Boolean.FALSE);

        when(depreciacaoDataProvider.existePorPatrimonioNoPeriodo(anyLong(), any(LocalDateTime.class)))
            .thenReturn(Boolean.FALSE);

        when(movimentacaoItemDataProvider.buscarUltimaMovimentacaoPatrimonioAntesDe(anyLong(), any(Date.class)))
            .thenReturn(Optional.of(MovimentacaoItem.builder().movimentacao(ultimaMovimentacao).build()));

        CalcularDepreciacaoOutputData outputData = usecase.executar(new CalcularDepreciacaoInputData(1L));

        assertEquals(patrimonioDepreciado, outputData.getPatrimonio());
        assertEquals(4, outputData.getDepreciacao().size());
        assertEquals(depreciacaoMarco, outputData.getDepreciacao().get(0));
        assertEquals(depreciacaoAbril, outputData.getDepreciacao().get(1));
        assertEquals(depreciacaoMaio, outputData.getDepreciacao().get(2));
        assertEquals(depreciacaoJunho, outputData.getDepreciacao().get(3));
        verify(patrimonioDataProvider, times(1)).buscarPorId(patrimonio.getId());
        verify(depreciacaoDataProvider, times(2)).existePorPatrimonio(patrimonio.getId());
        verify(depreciacaoDataProvider, times(1)).existePorPatrimonioNoPeriodo(patrimonio.getId(), patrimonio.getInicioVidaUtil());
        verify(movimentacaoItemDataProvider, times(1)).buscarUltimaMovimentacaoPatrimonioAntesDe(patrimonio.getId(), dateAtEndOfDay(periodoMarco.getDataFinal()));
    }

    @Test
    public void deveCalcularDepreciacaoUnica() {
        UnidadeOrganizacional orgao = UnidadeOrganizacional.builder().id(1L).build();
        UnidadeOrganizacional setor = UnidadeOrganizacional.builder().id(2L).build();

        Patrimonio patrimonio = Patrimonio.builder()
            .id(1L)
            .valorLiquido(BigDecimal.valueOf(35000))
            .valorEntrada(BigDecimal.valueOf(35000))
            .valorAquisicao(BigDecimal.valueOf(35000))
            .valorResidual(BigDecimal.valueOf(5000))
            .valorDepreciacaoMensal(new BigDecimal("10000.00").setScale(2, RoundingMode.HALF_EVEN))
            .fimVidaUtil(LocalDateTime.of(2020, 3, 24, 23, 59, 59))
            .inicioVidaUtil(LocalDateTime.of(2020, 1, 1, 0, 0, 0))
            .orgao(orgao)
            .setor(setor)
            .build();

        Patrimonio patrimonioDepreciado = Patrimonio.builder()
            .id(1L)
            .valorLiquido(new BigDecimal("5000.00").setScale(2, RoundingMode.HALF_EVEN))
            .valorEntrada(BigDecimal.valueOf(35000))
            .valorAquisicao(BigDecimal.valueOf(35000))
            .valorResidual(BigDecimal.valueOf(5000))
            .valorDepreciacaoMensal(new BigDecimal("10000.00").setScale(2, RoundingMode.HALF_EVEN))
            .fimVidaUtil(LocalDateTime.of(2020, 3, 24, 23, 59, 59))
            .inicioVidaUtil(LocalDateTime.of(2020, 1, 1, 0, 0, 0))
            .orgao(orgao)
            .setor(setor)
            .build();

        PeriodoDepreciacao periodoJaneiro = criarPeriodoDepreciacao(1, 31, 1);
        PeriodoDepreciacao periodoFevereiro = criarPeriodoDepreciacao(1, 29, 2);
        PeriodoDepreciacao periodoMarco = criarPeriodoDepreciacao(1, 24, 3);

        Movimentacao ultimaMovimentacao = Movimentacao.builder()
            .id(1L)
            .tipo(TipoMovimentacaoEnum.DISTRIBUICAO)
            .dataFinalizacao(LocalDateTime.of(periodoJaneiro.getDataInicial(), LocalTime.of(0, 0, 0)))
            .orgaoDestino(orgao)
            .setorDestino(setor)
            .build();

        Depreciacao depreciacaoJaneiro = Depreciacao.builder()
            .dataInicial(LocalDateTime.of(2020, 1, 1, 0, 0, 0))
            .dataFinal(LocalDateTime.of(2020, 1, 31, 23, 59, 59))
            .valorAnterior(BigDecimal.valueOf(35000))
            .mesReferencia("01/2020")
            .valorPosterior(new BigDecimal("25000.00").setScale(2, RoundingMode.HALF_EVEN))
            .valorSubtraido(new BigDecimal("10000.00").setScale(2, RoundingMode.HALF_EVEN))
            .taxaAplicada(new BigDecimal("33.33").setScale(2, RoundingMode.HALF_EVEN))
            .patrimonio(patrimonio)
            .orgao(orgao)
            .setor(setor)
            .build();

        Depreciacao depreciacaoFevereiro = criarDepreciacao(
            LocalDateTime.of(periodoFevereiro.getDataInicial(), LocalTime.of(0, 0, 0)),
            LocalDateTime.of(periodoFevereiro.getDataFinal(), LocalTime.of(23, 59, 59)),
            depreciacaoJaneiro,
            BigDecimal.valueOf(33.33).setScale(2, RoundingMode.HALF_EVEN)
        );
        Depreciacao depreciacaoMarco = criarDepreciacao(
            LocalDateTime.of(periodoMarco.getDataInicial(), LocalTime.of(0, 0, 0)),
            LocalDateTime.of(periodoMarco.getDataFinal(), LocalTime.of(23, 59, 59)),
            depreciacaoFevereiro,
            BigDecimal.valueOf(33.33).setScale(2, RoundingMode.HALF_EVEN)
        );

        when(patrimonioDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(patrimonio));

        when(depreciacaoDataProvider.existePorPatrimonioNoPeriodo(anyLong(), any(LocalDateTime.class)))
            .thenReturn(Boolean.FALSE);

        when(movimentacaoItemDataProvider.buscarUltimaMovimentacaoPatrimonioAntesDe(anyLong(), any(Date.class)))
            .thenReturn(Optional.of(MovimentacaoItem.builder().movimentacao(ultimaMovimentacao).build()));

        CalcularDepreciacaoOutputData outputData = usecase.executar(new CalcularDepreciacaoInputData(1L));

        assertEquals(patrimonioDepreciado, outputData.getPatrimonio());
        assertEquals(3, outputData.getDepreciacao().size());
        assertEquals(depreciacaoJaneiro, outputData.getDepreciacao().get(0));
        assertEquals(depreciacaoFevereiro, outputData.getDepreciacao().get(1));
        assertEquals(depreciacaoMarco, outputData.getDepreciacao().get(2));
        verify(patrimonioDataProvider, times(1)).buscarPorId(patrimonio.getId());
        verify(depreciacaoDataProvider, times(1)).existePorPatrimonio(patrimonio.getId());
        verify(depreciacaoDataProvider, times(1)).existePorPatrimonioNoPeriodo(patrimonio.getId(), patrimonio.getInicioVidaUtil());
        verify(movimentacaoItemDataProvider, times(1)).buscarUltimaMovimentacaoPatrimonioAntesDe(patrimonio.getId(), dateAtEndOfDay(periodoJaneiro.getDataFinal()));
        verify(movimentacaoItemDataProvider, times(1)).buscarUltimaMovimentacaoPatrimonioAntesDe(patrimonio.getId(), dateAtEndOfDay(periodoFevereiro.getDataFinal()));
        verify(movimentacaoItemDataProvider, times(1)).buscarUltimaMovimentacaoPatrimonioAntesDe(patrimonio.getId(), dateAtEndOfDay(periodoMarco.getDataFinal()));
    }

    private PeriodoDepreciacao criarPeriodoDepreciacao(int diaInicial, int diaFinal, int mes) {
        return PeriodoDepreciacao.builder()
            .dataInicial(LocalDate.of(2020, mes, diaInicial))
            .dataFinal(LocalDate.of(2020, mes, diaFinal))
            .build();
    }

    private Depreciacao criarDepreciacao(LocalDateTime dataInicio, LocalDateTime dataFinal, Depreciacao depreciacaoAnterior, BigDecimal taxaAplicada) {
        final BigDecimal valorDepreciacaoMensal = depreciacaoAnterior.getPatrimonio().getValorDepreciacaoMensal();
        return Depreciacao.builder()
            .dataInicial(dataInicio)
            .dataFinal(dataFinal)
            .mesReferencia(DateUtils.formatarData(dataInicio, "MM/yyyy"))
            .valorAnterior(depreciacaoAnterior.getValorPosterior())
            .valorPosterior(depreciacaoAnterior.getValorPosterior().subtract(valorDepreciacaoMensal))
            .valorSubtraido(depreciacaoAnterior.getPatrimonio().getValorDepreciacaoMensal())
            .taxaAplicada(taxaAplicada)
            .patrimonio(depreciacaoAnterior.getPatrimonio())
            .orgao(depreciacaoAnterior.getOrgao())
            .setor(depreciacaoAnterior.getSetor())
            .build();
    }

    private Date dateAtEndOfDay(LocalDate localDate) {
        return DateUtils.asDate(localDate.atTime(23, 59, 59));
    }
}
