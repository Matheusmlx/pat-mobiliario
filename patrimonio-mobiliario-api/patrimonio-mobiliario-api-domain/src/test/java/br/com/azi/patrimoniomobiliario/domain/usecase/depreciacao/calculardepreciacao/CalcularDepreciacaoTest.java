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
import org.junit.Ignore;
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
public class CalcularDepreciacaoTest {

    private static final LocalDate LOCAL_DATE = LocalDate.of(2020, 4, 1);
    private static final boolean DEPRECIA_EM_ALMOXARIFADO = true;
    private static final boolean NAO_DEPRECIA_EM_ALMOXARIFADO = false;

    @Mock
    private PatrimonioDataProvider patrimonioDataProvider;

    @Mock
    private DepreciacaoDataProvider depreciacaoDataProvider;

    @Mock
    private MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    @InjectMocks
    private CalcularDepreciacaoOutputDataConverter calcularDepreciacaoOutputDataConverter;

    private CalcularDepreciacaoUseCaseImpl usecase;

    public void instanciarUseCase(LocalDate dataDepreciacao, boolean depreciaEmAlmoxarifado) {
        final Clock fixedClock = Clock.fixed(dataDepreciacao.atStartOfDay(
            ZoneId.systemDefault()).toInstant(),
            ZoneId.systemDefault()
        );

        usecase = new CalcularDepreciacaoUseCaseImpl(
            fixedClock,
            patrimonioDataProvider,
            depreciacaoDataProvider,
            movimentacaoItemDataProvider,
            depreciaEmAlmoxarifado,
            calcularDepreciacaoOutputDataConverter);
    }

    @Test
    public void deveCalcularDepreciacaoParcialNoPrimeiroMesQuandoFoiDistribuidoAposPrimeiroDiaMes() {
        instanciarUseCase(LOCAL_DATE, NAO_DEPRECIA_EM_ALMOXARIFADO);

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

        Movimentacao ultimaMovimentacao = Movimentacao.builder()
            .id(1L)
            .tipo(TipoMovimentacaoEnum.DISTRIBUICAO)
            .dataFinalizacao(LocalDateTime.of(2020, 3, 15, 0, 0, 0))
            .orgaoDestino(orgao)
            .setorDestino(setor)
            .build();

        Depreciacao depreciacaoEsperada = Depreciacao.builder()
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

        PeriodoDepreciacao periodoDepreciacao = PeriodoDepreciacao.builder()
            .dataInicial(LocalDate.of(2020, 3, 15))
            .dataFinal(LocalDate.of(2020, 3, 31))
            .build();

        when(patrimonioDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(patrimonio));

        when(depreciacaoDataProvider.existePorPatrimonio(anyLong())).thenReturn(Boolean.FALSE);

        when(depreciacaoDataProvider.existePorPatrimonioNoPeriodo(anyLong(), any(LocalDateTime.class)))
            .thenReturn(Boolean.FALSE);

        when(movimentacaoItemDataProvider.buscarUltimaMovimentacaoPatrimonioAntesDe(anyLong(), any(Date.class)))
            .thenReturn(Optional.of(MovimentacaoItem.builder().movimentacao(ultimaMovimentacao).build()));

        when(movimentacaoItemDataProvider.existeDistribuicaoFinalizadaParaPatrimonioNoPeriodo(anyLong(), any(Date.class), any(Date.class)))
            .thenReturn(Boolean.TRUE);

        when(movimentacaoItemDataProvider.buscarUltimaMovimentacaoPatrimonioPorTipoNoPeriodo(anyLong(), any(TipoMovimentacaoEnum.class), any(Date.class), any(Date.class)))
            .thenReturn(Optional.of(MovimentacaoItem.builder().movimentacao(ultimaMovimentacao).build()));

        CalcularDepreciacaoOutputData outputData = usecase.executar(new CalcularDepreciacaoInputData(1L));

        assertEquals(1, outputData.getDepreciacao().size());
        assertEquals(depreciacaoEsperada, outputData.getDepreciacao().get(0));
        verify(patrimonioDataProvider, times(1)).buscarPorId(patrimonio.getId());
        verify(depreciacaoDataProvider, times(2)).existePorPatrimonio(patrimonio.getId());
        verify(depreciacaoDataProvider, times(1)).existePorPatrimonioNoPeriodo(patrimonio.getId(), periodoDepreciacao.getDataInicial().atStartOfDay());
        verify(movimentacaoItemDataProvider, times(1)).buscarUltimaMovimentacaoPatrimonioAntesDe(patrimonio.getId(), dateAtEndOfDay(periodoDepreciacao.getDataFinal()));
        verify(movimentacaoItemDataProvider, times(1)).existeDistribuicaoFinalizadaParaPatrimonioNoPeriodo(patrimonio.getId(), dateAtStartOfDay(periodoDepreciacao.getDataInicial()), dateAtEndOfDay(periodoDepreciacao.getDataFinal()));
        verify(movimentacaoItemDataProvider, times(1)).buscarUltimaMovimentacaoPatrimonioPorTipoNoPeriodo(patrimonio.getId(), TipoMovimentacaoEnum.DISTRIBUICAO, dateAtStartOfDay(periodoDepreciacao.getDataInicial()), dateAtEndOfDay(periodoDepreciacao.getDataFinal()));
    }

    @Test
    public void deveCalcularDepreciacaoMesCheioNoPrimeiroMesQuandoFoiDistribuidoNoPrimeiroDiaDoMes() {
        instanciarUseCase(LOCAL_DATE, NAO_DEPRECIA_EM_ALMOXARIFADO);

        UnidadeOrganizacional orgao = UnidadeOrganizacional.builder().id(1L).build();
        UnidadeOrganizacional setor = UnidadeOrganizacional.builder().id(2L).build();

        Patrimonio patrimonio = Patrimonio.builder()
            .id(1L)
            .valorLiquido(BigDecimal.valueOf(50000))
            .valorEntrada(BigDecimal.valueOf(50000))
            .valorAquisicao(BigDecimal.valueOf(50000))
            .valorResidual(BigDecimal.valueOf(5000))
            .valorDepreciacaoMensal(new BigDecimal("375.00").setScale(2, RoundingMode.HALF_EVEN))
            .fimVidaUtil(LocalDateTime.of(2030, 3, 24, 23, 59, 59))
            .inicioVidaUtil(LocalDateTime.of(2020, 3, 1, 0, 0, 0))
            .orgao(orgao)
            .setor(setor)
            .build();

        Patrimonio patrimonioDepreciado = Patrimonio.builder()
            .id(1L)
            .valorLiquido(new BigDecimal("49625.00").setScale(2, RoundingMode.HALF_EVEN))
            .valorEntrada(BigDecimal.valueOf(50000))
            .valorAquisicao(BigDecimal.valueOf(50000))
            .valorResidual(BigDecimal.valueOf(5000))
            .valorDepreciacaoMensal(new BigDecimal("375.00").setScale(2, RoundingMode.HALF_EVEN))
            .fimVidaUtil(LocalDateTime.of(2030, 3, 24, 23, 59, 59))
            .inicioVidaUtil(LocalDateTime.of(2020, 3, 1, 0, 0, 0))
            .orgao(orgao)
            .setor(setor)
            .build();

        Movimentacao ultimaMovimentacao = Movimentacao.builder()
            .id(1L)
            .tipo(TipoMovimentacaoEnum.DISTRIBUICAO)
            .dataFinalizacao(LocalDateTime.of(2020, 3, 1, 0, 0, 0))
            .orgaoDestino(orgao)
            .setorDestino(setor)
            .build();

        PeriodoDepreciacao periodoDepreciacao = PeriodoDepreciacao.builder()
            .dataInicial(LocalDate.of(2020, 3, 1))
            .dataFinal(LocalDate.of(2020, 3, 31))
            .build();

        when(patrimonioDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(patrimonio));

        when(depreciacaoDataProvider.existePorPatrimonio(patrimonio.getId())).thenReturn(Boolean.FALSE);

        when(depreciacaoDataProvider.existePorPatrimonioNoPeriodo(anyLong(), any(LocalDateTime.class)))
            .thenReturn(Boolean.FALSE);

        when(movimentacaoItemDataProvider.buscarUltimaMovimentacaoPatrimonioAntesDe(anyLong(), any(Date.class)))
            .thenReturn(Optional.of(MovimentacaoItem.builder().movimentacao(ultimaMovimentacao).build()));

        when(movimentacaoItemDataProvider.existeDistribuicaoFinalizadaParaPatrimonioNoPeriodo(anyLong(), any(Date.class), any(Date.class)))
            .thenReturn(Boolean.TRUE);

        when(movimentacaoItemDataProvider.buscarUltimaMovimentacaoPatrimonioPorTipoNoPeriodo(anyLong(), any(TipoMovimentacaoEnum.class), any(Date.class), any(Date.class)))
            .thenReturn(Optional.of(MovimentacaoItem.builder().movimentacao(ultimaMovimentacao).build()));

        Depreciacao depreciacaoEsperada = Depreciacao.builder()
            .dataInicial(LocalDateTime.of(2020, 3, 1, 0, 0, 0))
            .dataFinal(LocalDateTime.of(2020, 3, 31, 23, 59, 59))
            .mesReferencia("03/2020")
            .valorAnterior(BigDecimal.valueOf(50000))
            .valorPosterior(new BigDecimal("49625.00").setScale(2, RoundingMode.HALF_EVEN))
            .valorSubtraido(new BigDecimal("375.00").setScale(2, RoundingMode.HALF_EVEN))
            .taxaAplicada(new BigDecimal("0.83").setScale(2, RoundingMode.HALF_EVEN))
            .patrimonio(patrimonio)
            .orgao(orgao)
            .setor(setor)
            .build();

        CalcularDepreciacaoOutputData outputData = usecase.executar(new CalcularDepreciacaoInputData(1L));

        assertEquals(1, outputData.getDepreciacao().size());
        assertEquals(depreciacaoEsperada, outputData.getDepreciacao().get(0));
        assertEquals(patrimonioDepreciado, outputData.getPatrimonio());
        verify(patrimonioDataProvider, times(1)).buscarPorId(patrimonio.getId());
        verify(depreciacaoDataProvider, times(2)).existePorPatrimonio(patrimonio.getId());
        verify(depreciacaoDataProvider, times(1)).existePorPatrimonioNoPeriodo(patrimonio.getId(), periodoDepreciacao.getDataInicial().atStartOfDay());
        verify(movimentacaoItemDataProvider, times(1)).buscarUltimaMovimentacaoPatrimonioAntesDe(patrimonio.getId(), dateAtEndOfDay(periodoDepreciacao.getDataFinal()));
        verify(movimentacaoItemDataProvider, times(1)).existeDistribuicaoFinalizadaParaPatrimonioNoPeriodo(patrimonio.getId(), dateAtStartOfDay(periodoDepreciacao.getDataInicial()), dateAtEndOfDay(periodoDepreciacao.getDataFinal()));
        verify(movimentacaoItemDataProvider, times(1)).buscarUltimaMovimentacaoPatrimonioPorTipoNoPeriodo(patrimonio.getId(), TipoMovimentacaoEnum.DISTRIBUICAO, dateAtStartOfDay(periodoDepreciacao.getDataInicial()), dateAtEndOfDay(periodoDepreciacao.getDataFinal()));
    }

    @Test
    public void deveRetornarDepreciacaoComValorZeradoQuandoPatrimonioFinalizarOMesEmAlmoxarifado() {
        instanciarUseCase(LOCAL_DATE, NAO_DEPRECIA_EM_ALMOXARIFADO);

        UnidadeOrganizacional orgao = UnidadeOrganizacional.builder().id(1L).build();
        UnidadeOrganizacional setor = UnidadeOrganizacional.builder().id(2L).build();

        Patrimonio patrimonio = Patrimonio.builder()
            .id(1L)
            .valorLiquido(BigDecimal.valueOf(50000))
            .valorEntrada(BigDecimal.valueOf(50000))
            .valorAquisicao(BigDecimal.valueOf(50000))
            .valorResidual(BigDecimal.valueOf(5000))
            .valorDepreciacaoMensal(new BigDecimal("375.00").setScale(2, RoundingMode.HALF_EVEN))
            .fimVidaUtil(LocalDateTime.of(2030, 3, 24, 23, 59, 59))
            .inicioVidaUtil(LocalDateTime.of(2020, 3, 13, 0, 0, 0))
            .orgao(orgao)
            .setor(setor)
            .build();

        Movimentacao ultimaMovimentacao = Movimentacao.builder()
            .id(1L)
            .tipo(TipoMovimentacaoEnum.DEVOLUCAO_ALMOXARIFADO)
            .dataFinalizacao(LocalDateTime.of(2020, 3, 16, 0, 0, 0))
            .orgaoDestino(orgao)
            .setorDestino(setor)
            .build();

        PeriodoDepreciacao periodoDepreciacao = PeriodoDepreciacao.builder()
            .dataInicial(LocalDate.of(2020, 3, 13))
            .dataFinal(LocalDate.of(2020, 3, 31))
            .build();

        when(patrimonioDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(patrimonio));

        when(depreciacaoDataProvider.existePorPatrimonio(anyLong())).thenReturn(Boolean.FALSE);

        when(depreciacaoDataProvider.existePorPatrimonioNoPeriodo(anyLong(), any(LocalDateTime.class)))
            .thenReturn(Boolean.FALSE);

        when(movimentacaoItemDataProvider.buscarUltimaMovimentacaoPatrimonioAntesDe(anyLong(), any(Date.class)))
            .thenReturn(Optional.of(MovimentacaoItem.builder().movimentacao(ultimaMovimentacao).build()));

        Depreciacao depreciacaoEsperada = Depreciacao.builder()
            .dataInicial(LocalDateTime.of(2020, 3, 13, 0, 0, 0))
            .dataFinal(LocalDateTime.of(2020, 3, 31, 23, 59, 59))
            .mesReferencia("03/2020")
            .valorAnterior(BigDecimal.valueOf(50000))
            .valorPosterior(BigDecimal.valueOf(50000).setScale(2, RoundingMode.HALF_EVEN))
            .valorSubtraido(BigDecimal.ZERO)
            .taxaAplicada(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_EVEN))
            .patrimonio(patrimonio)
            .orgao(orgao)
            .setor(setor)
            .build();

        CalcularDepreciacaoOutputData outputData = usecase.executar(new CalcularDepreciacaoInputData(1L));

        assertEquals(1, outputData.getDepreciacao().size());
        assertEquals(depreciacaoEsperada, outputData.getDepreciacao().get(0));
        assertEquals(patrimonio, outputData.getPatrimonio());
        verify(patrimonioDataProvider, times(1)).buscarPorId(patrimonio.getId());
        verify(depreciacaoDataProvider, times(2)).existePorPatrimonio(patrimonio.getId());
        verify(depreciacaoDataProvider, times(1)).existePorPatrimonioNoPeriodo(patrimonio.getId(), periodoDepreciacao.getDataInicial().atStartOfDay());
        verify(movimentacaoItemDataProvider, times(1)).buscarUltimaMovimentacaoPatrimonioAntesDe(patrimonio.getId(), dateAtEndOfDay(periodoDepreciacao.getDataFinal()));
        verify(movimentacaoItemDataProvider, times(0)).existeDistribuicaoFinalizadaParaPatrimonioNoPeriodo(patrimonio.getId(), dateAtStartOfDay(periodoDepreciacao.getDataInicial()), dateAtEndOfDay(periodoDepreciacao.getDataFinal()));
        verify(movimentacaoItemDataProvider, times(0)).buscarUltimaMovimentacaoPatrimonioPorTipoNoPeriodo(patrimonio.getId(), TipoMovimentacaoEnum.DISTRIBUICAO, dateAtStartOfDay(periodoDepreciacao.getDataInicial()), dateAtEndOfDay(periodoDepreciacao.getDataFinal()));
    }

    @Test
    public void deveCalcularDepreciacaoMesCheioQuandoPatrimonioEmUsoENaoHouveRedistribuicaoNoPeriodo() {
        instanciarUseCase(LocalDate.of(2020, 5, 1), NAO_DEPRECIA_EM_ALMOXARIFADO);

        UnidadeOrganizacional orgao = UnidadeOrganizacional.builder().id(1L).build();
        UnidadeOrganizacional setor = UnidadeOrganizacional.builder().id(2L).build();

        Patrimonio patrimonio = Patrimonio.builder()
            .id(1L)
            .valorLiquido(BigDecimal.valueOf(49625))
            .valorEntrada(BigDecimal.valueOf(50000))
            .valorAquisicao(BigDecimal.valueOf(50000))
            .valorResidual(BigDecimal.valueOf(5000))
            .valorDepreciacaoMensal(new BigDecimal("375.00").setScale(2, RoundingMode.HALF_EVEN))
            .fimVidaUtil(LocalDateTime.of(2030, 3, 24, 23, 59, 59))
            .inicioVidaUtil(LocalDateTime.of(2020, 3, 1, 0, 0, 0))
            .orgao(orgao)
            .setor(setor)
            .build();

        Depreciacao primeiraDepreciacao = Depreciacao.builder()
            .dataInicial(LocalDateTime.of(2020, 3, 1, 0, 0, 0))
            .dataFinal(LocalDateTime.of(2020, 3, 31, 23, 59, 59))
            .mesReferencia("03/2020")
            .valorAnterior(BigDecimal.valueOf(50000))
            .valorPosterior(new BigDecimal("49625.00").setScale(2, RoundingMode.HALF_EVEN))
            .valorSubtraido(new BigDecimal("375.00").setScale(2, RoundingMode.HALF_EVEN))
            .taxaAplicada(new BigDecimal("0.83").setScale(2, RoundingMode.HALF_EVEN))
            .patrimonio(patrimonio)
            .orgao(orgao)
            .setor(setor)
            .build();

        Patrimonio patrimonioDepreciado = Patrimonio.builder()
            .id(1L)
            .valorLiquido(new BigDecimal("49250.00").setScale(2, RoundingMode.HALF_EVEN))
            .valorEntrada(BigDecimal.valueOf(50000))
            .valorAquisicao(BigDecimal.valueOf(50000))
            .valorResidual(BigDecimal.valueOf(5000))
            .valorDepreciacaoMensal(new BigDecimal("375.00").setScale(2, RoundingMode.HALF_EVEN))
            .fimVidaUtil(LocalDateTime.of(2030, 3, 24, 23, 59, 59))
            .inicioVidaUtil(LocalDateTime.of(2020, 3, 1, 0, 0, 0))
            .orgao(orgao)
            .setor(setor)
            .build();

        Movimentacao ultimaMovimentacao = Movimentacao.builder()
            .id(1L)
            .tipo(TipoMovimentacaoEnum.ENTRE_SETORES)
            .dataFinalizacao(LocalDateTime.of(2020, 3, 16, 0, 0, 0))
            .orgaoDestino(orgao)
            .setorDestino(setor)
            .build();

        PeriodoDepreciacao periodoDepreciacao = PeriodoDepreciacao.builder()
            .dataInicial(LocalDate.of(2020, 4, 1))
            .dataFinal(LocalDate.of(2020, 4, 30))
            .build();

        when(patrimonioDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(patrimonio));

        when(depreciacaoDataProvider.existePorPatrimonio(anyLong()))
            .thenReturn(Boolean.TRUE);

        when(depreciacaoDataProvider.existePorPatrimonioNoPeriodo(anyLong(), any(LocalDateTime.class))).thenReturn(Boolean.FALSE);

        when(depreciacaoDataProvider.buscarUltimaPorPatrimonio(anyLong()))
            .thenReturn(Optional.of(primeiraDepreciacao));

        when(movimentacaoItemDataProvider.buscarUltimaMovimentacaoPatrimonioAntesDe(anyLong(), any(Date.class)))
            .thenReturn(Optional.of(MovimentacaoItem.builder().movimentacao(ultimaMovimentacao).build()));

        when(movimentacaoItemDataProvider.existeDistribuicaoFinalizadaParaPatrimonioNoPeriodo(anyLong(), any(Date.class), any(Date.class)))
            .thenReturn(Boolean.FALSE);

        Depreciacao depreciacaoEsperada = Depreciacao.builder()
            .dataInicial(LocalDateTime.of(2020, 4, 1, 0, 0, 0))
            .dataFinal(LocalDateTime.of(2020, 4, 30, 23, 59, 59))
            .mesReferencia("04/2020")
            .valorAnterior(BigDecimal.valueOf(49625))
            .valorPosterior(new BigDecimal("49250.00").setScale(2, RoundingMode.HALF_EVEN))
            .valorSubtraido(new BigDecimal("375.00").setScale(2, RoundingMode.HALF_EVEN))
            .taxaAplicada(new BigDecimal("0.83").setScale(2, RoundingMode.HALF_EVEN))
            .patrimonio(patrimonio)
            .orgao(orgao)
            .setor(setor)
            .build();

        CalcularDepreciacaoOutputData outputData = usecase.executar(new CalcularDepreciacaoInputData(1L));

        assertEquals(1, outputData.getDepreciacao().size());
        assertEquals(depreciacaoEsperada, outputData.getDepreciacao().get(0));
        assertEquals(patrimonioDepreciado, outputData.getPatrimonio());
        verify(patrimonioDataProvider, times(1)).buscarPorId(patrimonio.getId());
        verify(depreciacaoDataProvider, times(2)).existePorPatrimonio(patrimonio.getId());
        verify(depreciacaoDataProvider, times(1)).existePorPatrimonioNoPeriodo(patrimonio.getId(), periodoDepreciacao.getDataInicial().atStartOfDay());
        verify(depreciacaoDataProvider, times(1)).buscarUltimaPorPatrimonio(patrimonio.getId());
        verify(movimentacaoItemDataProvider, times(1)).buscarUltimaMovimentacaoPatrimonioAntesDe(patrimonio.getId(), dateAtEndOfDay(periodoDepreciacao.getDataFinal()));
        verify(movimentacaoItemDataProvider, times(1)).existeDistribuicaoFinalizadaParaPatrimonioNoPeriodo(patrimonio.getId(), dateAtStartOfDay(periodoDepreciacao.getDataInicial()), dateAtEndOfDay(periodoDepreciacao.getDataFinal()));
        verify(movimentacaoItemDataProvider, times(0)).buscarUltimaMovimentacaoPatrimonioPorTipoNoPeriodo(patrimonio.getId(), TipoMovimentacaoEnum.DISTRIBUICAO, dateAtStartOfDay(periodoDepreciacao.getDataInicial()), dateAtEndOfDay(periodoDepreciacao.getDataFinal()));
    }

    @Test
    public void deveCalcularDepreciacaoDeMesCheioMesmoEstandoEmAlmoxarifado() {
        instanciarUseCase(LOCAL_DATE, DEPRECIA_EM_ALMOXARIFADO);

        UnidadeOrganizacional orgao = UnidadeOrganizacional.builder().id(1L).build();
        UnidadeOrganizacional setor = UnidadeOrganizacional.builder().id(2L).build();

        Patrimonio patrimonio = Patrimonio.builder()
            .id(1L)
            .valorLiquido(BigDecimal.valueOf(50000))
            .valorEntrada(BigDecimal.valueOf(50000))
            .valorAquisicao(BigDecimal.valueOf(50000))
            .valorResidual(BigDecimal.valueOf(5000))
            .valorDepreciacaoMensal(new BigDecimal("375.00").setScale(2, RoundingMode.HALF_EVEN))
            .fimVidaUtil(LocalDateTime.of(2030, 3, 31, 23, 59, 59))
            .inicioVidaUtil(LocalDateTime.of(2020, 3, 1, 0, 0, 0))
            .orgao(orgao)
            .setor(setor)
            .build();

        Movimentacao ultimaMovimentacao = Movimentacao.builder()
            .id(1L)
            .tipo(TipoMovimentacaoEnum.DEVOLUCAO_ALMOXARIFADO)
            .dataFinalizacao(LocalDateTime.of(2020, 3, 16, 0, 0, 0))
            .orgaoDestino(orgao)
            .setorDestino(setor)
            .build();

        PeriodoDepreciacao periodoDepreciacao = PeriodoDepreciacao.builder()
            .dataInicial(LocalDate.of(2020, 3, 1))
            .dataFinal(LocalDate.of(2020, 3, 31))
            .build();

        when(patrimonioDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(patrimonio));

        when(depreciacaoDataProvider.existePorPatrimonio(anyLong())).thenReturn(Boolean.FALSE);

        when(depreciacaoDataProvider.existePorPatrimonioNoPeriodo(anyLong(), any(LocalDateTime.class)))
            .thenReturn(Boolean.FALSE);

        when(movimentacaoItemDataProvider.buscarUltimaMovimentacaoPatrimonioAntesDe(anyLong(), any(Date.class)))
            .thenReturn(Optional.of(MovimentacaoItem.builder().movimentacao(ultimaMovimentacao).build()));

        Depreciacao depreciacaoEsperada = Depreciacao.builder()
            .dataInicial(LocalDateTime.of(2020, 3, 1, 0, 0, 0))
            .dataFinal(LocalDateTime.of(2020, 3, 31, 23, 59, 59))
            .mesReferencia("03/2020")
            .valorAnterior(BigDecimal.valueOf(50000))
            .valorPosterior(BigDecimal.valueOf(49625).setScale(2, RoundingMode.HALF_EVEN))
            .valorSubtraido(BigDecimal.valueOf(375).setScale(2, RoundingMode.HALF_EVEN))
            .taxaAplicada(BigDecimal.valueOf(0.83))
            .patrimonio(patrimonio)
            .orgao(orgao)
            .setor(setor)
            .build();

        Patrimonio patrimonioDepreciado = Patrimonio.builder()
            .id(1L)
            .valorLiquido(BigDecimal.valueOf(49625).setScale(2, RoundingMode.HALF_EVEN))
            .valorEntrada(BigDecimal.valueOf(50000))
            .valorAquisicao(BigDecimal.valueOf(50000))
            .valorResidual(BigDecimal.valueOf(5000))
            .valorDepreciacaoMensal(new BigDecimal("375.00").setScale(2, RoundingMode.HALF_EVEN))
            .fimVidaUtil(LocalDateTime.of(2030, 3, 31, 23, 59, 59))
            .inicioVidaUtil(LocalDateTime.of(2020, 3, 1, 0, 0, 0))
            .orgao(orgao)
            .setor(setor)
            .build();

        CalcularDepreciacaoOutputData outputData = usecase.executar(new CalcularDepreciacaoInputData(1L));

        assertEquals(1, outputData.getDepreciacao().size());
        assertEquals(depreciacaoEsperada, outputData.getDepreciacao().get(0));
        assertEquals(patrimonioDepreciado, outputData.getPatrimonio());
        verify(patrimonioDataProvider, times(1)).buscarPorId(patrimonio.getId());
        verify(depreciacaoDataProvider, times(2)).existePorPatrimonio(patrimonio.getId());
        verify(depreciacaoDataProvider, times(1)).existePorPatrimonioNoPeriodo(patrimonio.getId(), periodoDepreciacao.getDataInicial().atStartOfDay());
        verify(movimentacaoItemDataProvider, times(1)).buscarUltimaMovimentacaoPatrimonioAntesDe(patrimonio.getId(), dateAtEndOfDay(periodoDepreciacao.getDataFinal()));
        verify(movimentacaoItemDataProvider, times(0)).existeDistribuicaoFinalizadaParaPatrimonioNoPeriodo(patrimonio.getId(), dateAtStartOfDay(periodoDepreciacao.getDataInicial()), dateAtEndOfDay(periodoDepreciacao.getDataFinal()));
        verify(movimentacaoItemDataProvider, times(0)).buscarUltimaMovimentacaoPatrimonioPorTipoNoPeriodo(patrimonio.getId(), TipoMovimentacaoEnum.DISTRIBUICAO, dateAtStartOfDay(periodoDepreciacao.getDataInicial()), dateAtEndOfDay(periodoDepreciacao.getDataFinal()));
    }

    @Test
    public void deveCalcularDepreciacaoParcialMesmoEstandoEmAlmoxarifado() {
        instanciarUseCase(LOCAL_DATE, DEPRECIA_EM_ALMOXARIFADO);

        UnidadeOrganizacional orgao = UnidadeOrganizacional.builder().id(1L).build();
        UnidadeOrganizacional setor = UnidadeOrganizacional.builder().id(2L).build();

        Patrimonio patrimonio = Patrimonio.builder()
            .id(1L)
            .valorLiquido(BigDecimal.valueOf(50000))
            .valorEntrada(BigDecimal.valueOf(50000))
            .valorAquisicao(BigDecimal.valueOf(50000))
            .valorResidual(BigDecimal.valueOf(5000))
            .valorDepreciacaoMensal(new BigDecimal("375.00").setScale(2, RoundingMode.HALF_EVEN))
            .fimVidaUtil(LocalDateTime.of(2030, 3, 31, 23, 59, 59))
            .inicioVidaUtil(LocalDateTime.of(2020, 3, 13, 0, 0, 0))
            .orgao(orgao)
            .setor(setor)
            .build();

        Movimentacao ultimaMovimentacao = Movimentacao.builder()
            .id(1L)
            .tipo(TipoMovimentacaoEnum.DEVOLUCAO_ALMOXARIFADO)
            .dataFinalizacao(LocalDateTime.of(2020, 3, 16, 0, 0, 0))
            .orgaoDestino(orgao)
            .setorDestino(setor)
            .build();

        PeriodoDepreciacao periodoDepreciacao = PeriodoDepreciacao.builder()
            .dataInicial(LocalDate.of(2020, 3, 13))
            .dataFinal(LocalDate.of(2020, 3, 31))
            .build();

        when(patrimonioDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(patrimonio));

        when(depreciacaoDataProvider.existePorPatrimonio(anyLong())).thenReturn(Boolean.FALSE);

        when(depreciacaoDataProvider.existePorPatrimonioNoPeriodo(anyLong(), any(LocalDateTime.class)))
            .thenReturn(Boolean.FALSE);

        when(movimentacaoItemDataProvider.buscarUltimaMovimentacaoPatrimonioAntesDe(anyLong(), any(Date.class)))
            .thenReturn(Optional.of(MovimentacaoItem.builder().movimentacao(ultimaMovimentacao).build()));

        Depreciacao depreciacaoEsperada = Depreciacao.builder()
            .dataInicial(LocalDateTime.of(2020, 3, 13, 0, 0, 0))
            .dataFinal(LocalDateTime.of(2020, 3, 31, 23, 59, 59))
            .mesReferencia("03/2020")
            .valorAnterior(BigDecimal.valueOf(50000))
            .valorPosterior(BigDecimal.valueOf(49762.50).setScale(2, RoundingMode.HALF_EVEN))
            .valorSubtraido(BigDecimal.valueOf(237.50).setScale(2, RoundingMode.HALF_EVEN))
            .taxaAplicada(BigDecimal.valueOf(0.53))
            .patrimonio(patrimonio)
            .orgao(orgao)
            .setor(setor)
            .build();

        Patrimonio patrimonioDepreciado = Patrimonio.builder()
            .id(1L)
            .valorLiquido(BigDecimal.valueOf(49762.50).setScale(2, RoundingMode.HALF_EVEN))
            .valorEntrada(BigDecimal.valueOf(50000))
            .valorAquisicao(BigDecimal.valueOf(50000))
            .valorResidual(BigDecimal.valueOf(5000))
            .valorDepreciacaoMensal(new BigDecimal("375.00").setScale(2, RoundingMode.HALF_EVEN))
            .fimVidaUtil(LocalDateTime.of(2030, 3, 31, 23, 59, 59))
            .inicioVidaUtil(LocalDateTime.of(2020, 3, 13, 0, 0, 0))
            .orgao(orgao)
            .setor(setor)
            .build();

        CalcularDepreciacaoOutputData outputData = usecase.executar(new CalcularDepreciacaoInputData(1L));

        assertEquals(1, outputData.getDepreciacao().size());
        assertEquals(depreciacaoEsperada, outputData.getDepreciacao().get(0));
        assertEquals(patrimonioDepreciado, outputData.getPatrimonio());
        verify(patrimonioDataProvider, times(1)).buscarPorId(patrimonio.getId());
        verify(depreciacaoDataProvider, times(2)).existePorPatrimonio(patrimonio.getId());
        verify(depreciacaoDataProvider, times(1)).existePorPatrimonioNoPeriodo(patrimonio.getId(), periodoDepreciacao.getDataInicial().atStartOfDay());
        verify(movimentacaoItemDataProvider, times(1)).buscarUltimaMovimentacaoPatrimonioAntesDe(patrimonio.getId(), dateAtEndOfDay(periodoDepreciacao.getDataFinal()));
        verify(movimentacaoItemDataProvider, times(0)).existeDistribuicaoFinalizadaParaPatrimonioNoPeriodo(patrimonio.getId(), dateAtStartOfDay(periodoDepreciacao.getDataInicial()), dateAtEndOfDay(periodoDepreciacao.getDataFinal()));
        verify(movimentacaoItemDataProvider, times(0)).buscarUltimaMovimentacaoPatrimonioPorTipoNoPeriodo(patrimonio.getId(), TipoMovimentacaoEnum.DISTRIBUICAO, dateAtStartOfDay(periodoDepreciacao.getDataInicial()), dateAtEndOfDay(periodoDepreciacao.getDataFinal()));
    }

    @Test
    public void deveCalcularDepreciacaoRestanteQuandoAtingirUltimoMesVidaUtil() {
        instanciarUseCase(LocalDate.of(2030, 4, 1), NAO_DEPRECIA_EM_ALMOXARIFADO);

        UnidadeOrganizacional orgao = UnidadeOrganizacional.builder().id(1L).build();
        UnidadeOrganizacional setor = UnidadeOrganizacional.builder().id(2L).build();

        Patrimonio patrimonio = Patrimonio.builder()
            .id(1L)
            .valorLiquido(BigDecimal.valueOf(5850))
            .valorEntrada(BigDecimal.valueOf(50000))
            .valorAquisicao(BigDecimal.valueOf(50000))
            .valorResidual(BigDecimal.valueOf(5000))
            .valorDepreciacaoMensal(new BigDecimal("375.00").setScale(2, RoundingMode.HALF_EVEN))
            .fimVidaUtil(LocalDateTime.of(2030, 3, 31, 23, 59, 59))
            .inicioVidaUtil(LocalDateTime.of(2020, 4, 1, 0, 0, 0))
            .orgao(orgao)
            .setor(setor)
            .build();

        Depreciacao ultimaDepreciacao = Depreciacao.builder()
            .dataInicial(LocalDateTime.of(2030, 2, 1, 0, 0, 0))
            .dataFinal(LocalDateTime.of(2030, 2, 28, 23, 59, 59))
            .mesReferencia("02/2030")
            .valorAnterior(BigDecimal.valueOf(6600))
            .valorPosterior(new BigDecimal("6225.00").setScale(2, RoundingMode.HALF_EVEN))
            .valorSubtraido(new BigDecimal("375.00").setScale(2, RoundingMode.HALF_EVEN))
            .taxaAplicada(new BigDecimal("0.83").setScale(2, RoundingMode.HALF_EVEN))
            .patrimonio(patrimonio)
            .orgao(orgao)
            .setor(setor)
            .build();

        Patrimonio patrimonioDepreciado = Patrimonio.builder()
            .id(1L)
            .valorLiquido(new BigDecimal("5000.00").setScale(2, RoundingMode.HALF_EVEN))
            .valorEntrada(BigDecimal.valueOf(50000))
            .valorAquisicao(BigDecimal.valueOf(50000))
            .valorResidual(BigDecimal.valueOf(5000))
            .valorDepreciacaoMensal(new BigDecimal("375.00").setScale(2, RoundingMode.HALF_EVEN))
            .fimVidaUtil(LocalDateTime.of(2030, 3, 31, 23, 59, 59))
            .inicioVidaUtil(LocalDateTime.of(2020, 4, 1, 0, 0, 0))
            .orgao(orgao)
            .setor(setor)
            .build();

        Movimentacao ultimaMovimentacao = Movimentacao.builder()
            .id(1L)
            .tipo(TipoMovimentacaoEnum.ENTRE_SETORES)
            .dataFinalizacao(LocalDateTime.of(2020, 3, 16, 0, 0, 0))
            .orgaoDestino(orgao)
            .setorDestino(setor)
            .build();

        PeriodoDepreciacao periodoDepreciacao = PeriodoDepreciacao.builder()
            .dataInicial(LocalDate.of(2030, 3, 1))
            .dataFinal(LocalDate.of(2030, 3, 31))
            .build();

        when(patrimonioDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(patrimonio));

        when(depreciacaoDataProvider.existePorPatrimonio(anyLong()))
            .thenReturn(Boolean.TRUE);

        when(depreciacaoDataProvider.existePorPatrimonioNoPeriodo(anyLong(), any(LocalDateTime.class)))
            .thenReturn(Boolean.FALSE);

        when(depreciacaoDataProvider.buscarUltimaPorPatrimonio(anyLong()))
            .thenReturn(Optional.of(ultimaDepreciacao));

        when(movimentacaoItemDataProvider.buscarUltimaMovimentacaoPatrimonioAntesDe(anyLong(), any(Date.class)))
            .thenReturn(Optional.of(MovimentacaoItem.builder().movimentacao(ultimaMovimentacao).build()));

        Depreciacao depreciacaoEsperada = Depreciacao.builder()
            .dataInicial(LocalDateTime.of(2030, 3, 1, 0, 0, 0))
            .dataFinal(LocalDateTime.of(2030, 3, 31, 23, 59, 59))
            .mesReferencia("03/2030")
            .valorAnterior(BigDecimal.valueOf(5850))
            .valorPosterior(new BigDecimal("5000.00").setScale(2, RoundingMode.HALF_EVEN))
            .valorSubtraido(new BigDecimal(850))
            .taxaAplicada(new BigDecimal("1.89").setScale(2, RoundingMode.HALF_EVEN))
            .patrimonio(patrimonio)
            .orgao(orgao)
            .setor(setor)
            .build();

        CalcularDepreciacaoOutputData outputData = usecase.executar(new CalcularDepreciacaoInputData(1L));

        assertEquals(1, outputData.getDepreciacao().size());
        assertEquals(depreciacaoEsperada, outputData.getDepreciacao().get(0));
        assertEquals(patrimonioDepreciado, outputData.getPatrimonio());
        verify(patrimonioDataProvider, times(1)).buscarPorId(patrimonio.getId());
        verify(depreciacaoDataProvider, times(2)).existePorPatrimonio(patrimonio.getId());
        verify(depreciacaoDataProvider, times(1)).existePorPatrimonioNoPeriodo(patrimonio.getId(), periodoDepreciacao.getDataInicial().atStartOfDay());
        verify(depreciacaoDataProvider, times(1)).buscarUltimaPorPatrimonio(patrimonio.getId());
        verify(movimentacaoItemDataProvider, times(1)).buscarUltimaMovimentacaoPatrimonioAntesDe(patrimonio.getId(), dateAtEndOfDay(periodoDepreciacao.getDataFinal()));
        verify(movimentacaoItemDataProvider, times(0)).existeDistribuicaoFinalizadaParaPatrimonioNoPeriodo(patrimonio.getId(), dateAtStartOfDay(periodoDepreciacao.getDataInicial()), dateAtEndOfDay(periodoDepreciacao.getDataFinal()));
        verify(movimentacaoItemDataProvider, times(0)).buscarUltimaMovimentacaoPatrimonioPorTipoNoPeriodo(patrimonio.getId(), TipoMovimentacaoEnum.DISTRIBUICAO, dateAtStartOfDay(periodoDepreciacao.getDataInicial()), dateAtEndOfDay(periodoDepreciacao.getDataFinal()));
    }

    @Test
    public void naoDeveCalcularDepreciacaoQuandoPatrimonioJaAtingiuValorResidual() {
        instanciarUseCase(LocalDate.of(2030, 5, 1), NAO_DEPRECIA_EM_ALMOXARIFADO);

        UnidadeOrganizacional orgao = UnidadeOrganizacional.builder().id(1L).build();
        UnidadeOrganizacional setor = UnidadeOrganizacional.builder().id(2L).build();

        Patrimonio patrimonio = Patrimonio.builder()
            .id(1L)
            .valorLiquido(BigDecimal.valueOf(5000))
            .valorEntrada(BigDecimal.valueOf(50000))
            .valorAquisicao(BigDecimal.valueOf(50000))
            .valorResidual(BigDecimal.valueOf(5000))
            .valorDepreciacaoMensal(new BigDecimal("375.00").setScale(2, RoundingMode.HALF_EVEN))
            .fimVidaUtil(LocalDateTime.of(2030, 3, 31, 23, 59, 59))
            .inicioVidaUtil(LocalDateTime.of(2020, 4, 1, 0, 0, 0))
            .orgao(orgao)
            .setor(setor)
            .build();

        Depreciacao ultimaDepreciacao = Depreciacao.builder()
            .dataInicial(LocalDateTime.of(2030, 3, 1, 0, 0, 0))
            .dataFinal(LocalDateTime.of(2030, 3, 31, 23, 59, 59))
            .mesReferencia("03/2030")
            .valorAnterior(BigDecimal.valueOf(5850))
            .valorPosterior(new BigDecimal("5000.00").setScale(2, RoundingMode.HALF_EVEN))
            .valorSubtraido(new BigDecimal(850))
            .taxaAplicada(new BigDecimal("1.89").setScale(2, RoundingMode.HALF_EVEN))
            .patrimonio(patrimonio)
            .orgao(orgao)
            .setor(setor)
            .build();

        when(patrimonioDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(patrimonio));

        when(depreciacaoDataProvider.existePorPatrimonio(anyLong()))
            .thenReturn(Boolean.TRUE);

        when(depreciacaoDataProvider.buscarUltimaPorPatrimonio(anyLong()))
            .thenReturn(Optional.of(ultimaDepreciacao));

        CalcularDepreciacaoOutputData outputData = usecase.executar(new CalcularDepreciacaoInputData(1L));

        assertEquals(0, outputData.getDepreciacao().size());
        assertEquals(patrimonio, outputData.getPatrimonio());
    }

    @Test
    public void naoDeveGerarDepreciacaoQuandoJaExistirDepreciacaoParaOPeriodo() {
        instanciarUseCase(LocalDate.of(2020, 4, 1), NAO_DEPRECIA_EM_ALMOXARIFADO);

        UnidadeOrganizacional orgao = UnidadeOrganizacional.builder().id(1L).build();
        UnidadeOrganizacional setor = UnidadeOrganizacional.builder().id(2L).build();

        Patrimonio patrimonio = Patrimonio.builder()
            .id(1L)
            .valorLiquido(new BigDecimal("49625.00").setScale(2, RoundingMode.HALF_EVEN))
            .valorEntrada(BigDecimal.valueOf(50000))
            .valorAquisicao(BigDecimal.valueOf(50000))
            .valorResidual(BigDecimal.valueOf(5000))
            .valorDepreciacaoMensal(new BigDecimal("375.00").setScale(2, RoundingMode.HALF_EVEN))
            .fimVidaUtil(LocalDateTime.of(2030, 3, 24, 23, 59, 59))
            .inicioVidaUtil(LocalDateTime.of(2020, 3, 1, 0, 0, 0))
            .orgao(orgao)
            .setor(setor)
            .build();

        when(patrimonioDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(patrimonio));

        when(depreciacaoDataProvider.existePorPatrimonio(patrimonio.getId())).thenReturn(Boolean.FALSE);

        when(depreciacaoDataProvider.existePorPatrimonioNoPeriodo(anyLong(), any(LocalDateTime.class)))
            .thenReturn(Boolean.TRUE);

        CalcularDepreciacaoOutputData outputData = usecase.executar(new CalcularDepreciacaoInputData(1L));

        assertEquals(0, outputData.getDepreciacao().size());
        assertEquals(patrimonio, outputData.getPatrimonio());
    }

    private Date dateAtStartOfDay(LocalDate localDate) {
        return DateUtils.asDate(localDate.atStartOfDay());
    }

    private Date dateAtEndOfDay(LocalDate localDate) {
        return DateUtils.asDate(localDate.atTime(23, 59, 59));
    }
}
