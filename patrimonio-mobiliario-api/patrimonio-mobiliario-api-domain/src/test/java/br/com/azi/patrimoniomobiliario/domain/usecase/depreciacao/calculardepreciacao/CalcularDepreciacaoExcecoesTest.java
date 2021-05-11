package br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.calculardepreciacao;

import br.com.azi.patrimoniomobiliario.domain.constant.movimentacao.TipoMovimentacaoEnum;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.MovimentacaoItem;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.DepreciacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.calculardepreciacao.converter.CalcularDepreciacaoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.calculardepreciacao.exception.DistribuicaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.calculardepreciacao.exception.PatrimonioNaoEncontradoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.calculardepreciacao.exception.UltimaDepreciacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.calculardepreciacao.exception.UltimaMovimentacaoNaoEncontradaException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CalcularDepreciacaoExcecoesTest {

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

    @Test(expected = PatrimonioNaoEncontradoException.class)
    public void deveFalharQuandoNaoEncontrarPatrimonio() {
        instanciarUseCase(LOCAL_DATE, DEPRECIA_EM_ALMOXARIFADO);

        when(patrimonioDataProvider.buscarPorId(anyLong())).thenReturn(Optional.empty());

        usecase.executar(new CalcularDepreciacaoInputData(1L));
    }

    @Test(expected = UltimaDepreciacaoNaoEncontradaException.class)
    public void deveFalharQuandoPatrimonioJaDepreciouENaoEncontrarUltimaDepreciacaoDoPatrimonio() {
        instanciarUseCase(LOCAL_DATE, DEPRECIA_EM_ALMOXARIFADO);

        Patrimonio patrimonio = Patrimonio.builder()
            .id(1L)
            .valorLiquido(BigDecimal.valueOf(50000))
            .valorEntrada(BigDecimal.valueOf(50000))
            .valorAquisicao(BigDecimal.valueOf(50000))
            .valorResidual(BigDecimal.valueOf(5000))
            .valorDepreciacaoMensal(BigDecimal.valueOf(375))
            .fimVidaUtil(LocalDateTime.of(2030, 3, 24, 23, 59, 59))
            .inicioVidaUtil(LocalDateTime.of(2020, 3, 15, 0, 0, 0))
            .build();

        when(patrimonioDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(patrimonio));

        when(depreciacaoDataProvider.existePorPatrimonio(anyLong())).thenReturn(true);

        when(depreciacaoDataProvider.buscarUltimaPorPatrimonio(anyLong())).thenReturn(Optional.empty());

        usecase.executar(new CalcularDepreciacaoInputData(1L));
    }

    @Test(expected = UltimaMovimentacaoNaoEncontradaException.class)
    public void deveFalharQuandoNaoEncontrarUltimaMovimentacaoDoPatrimonioNoPeriodo() {
        instanciarUseCase(LOCAL_DATE, DEPRECIA_EM_ALMOXARIFADO);

        Patrimonio patrimonio = Patrimonio.builder()
            .id(1L)
            .valorLiquido(BigDecimal.valueOf(50000))
            .valorEntrada(BigDecimal.valueOf(50000))
            .valorAquisicao(BigDecimal.valueOf(50000))
            .valorResidual(BigDecimal.valueOf(5000))
            .valorDepreciacaoMensal(BigDecimal.valueOf(375))
            .fimVidaUtil(LocalDateTime.of(2030, 3, 24, 23, 59, 59))
            .inicioVidaUtil(LocalDateTime.of(2020, 3, 15, 0, 0, 0))
            .build();

        when(patrimonioDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(patrimonio));

        when(depreciacaoDataProvider.existePorPatrimonio(anyLong())).thenReturn(false);

        when(movimentacaoItemDataProvider.buscarUltimaMovimentacaoPatrimonioAntesDe(anyLong(), any(Date.class)))
            .thenReturn(Optional.empty());

        usecase.executar(new CalcularDepreciacaoInputData(1L));
    }

    @Test(expected = DistribuicaoNaoEncontradaException.class)
    public void deveFalharQuandoNaoConseguirRecuperarDistribuicaoNoPeriodoQueOPatrimonioFoiDistribuido() {
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
            .thenReturn(Optional.empty());

        usecase.executar(new CalcularDepreciacaoInputData(1L));
    }
}
