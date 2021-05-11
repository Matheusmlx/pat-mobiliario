package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.buscarporitemincorporacao;

import br.com.azi.patrimoniomobiliario.domain.entity.ContaContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagem.BuscarPatrimoniosPorItemIncorporacaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagem.BuscarPatrimoniosPorItemIncorporacaoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagem.BuscarPatrimoniosPorItemIncorporacaoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagem.BuscarPatrimoniosPorItemIncorporacaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagem.converter.BuscarPatrimoniosPorItemIncorporacaoFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagem.converter.BuscarPatrimoniosPorItemIncorporacaoOutputDataConverter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class BuscarPatrimoniosPorItemIncorporacaoUseCaseTest {

    private PatrimonioDataProvider patrimonioDataProvider;

    private SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    @Before
    public void setUp() {
        patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        sistemaDeArquivosIntegration = Mockito.mock(SistemaDeArquivosIntegration.class);
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoNaoTiverQuantidadeItensPorPagina() {
        BuscarPatrimoniosPorItemIncorporacaoUseCase useCase = new BuscarPatrimoniosPorItemIncorporacaoUseCaseImpl(
            patrimonioDataProvider,
            new BuscarPatrimoniosPorItemIncorporacaoFiltroConverter(),
            new BuscarPatrimoniosPorItemIncorporacaoOutputDataConverter(),
            sistemaDeArquivosIntegration
        );

        BuscarPatrimoniosPorItemIncorporacaoInputData inputData = BuscarPatrimoniosPorItemIncorporacaoInputData
            .builder()
            .itemIncorporacaoId(2L)
            .page(5L)
            .build();

        useCase.executar(inputData);
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoNaoTiverNumeroDaPagina() {
        BuscarPatrimoniosPorItemIncorporacaoUseCase useCase = new BuscarPatrimoniosPorItemIncorporacaoUseCaseImpl(
            patrimonioDataProvider,
            new BuscarPatrimoniosPorItemIncorporacaoFiltroConverter(),
            new BuscarPatrimoniosPorItemIncorporacaoOutputDataConverter(),
            sistemaDeArquivosIntegration
        );

        BuscarPatrimoniosPorItemIncorporacaoInputData inputData = BuscarPatrimoniosPorItemIncorporacaoInputData
            .builder()
            .itemIncorporacaoId(7L)
            .size(15L)
            .build();

        useCase.executar(inputData);
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoItemIncorporacaoForNula() {
        BuscarPatrimoniosPorItemIncorporacaoUseCase useCase = new BuscarPatrimoniosPorItemIncorporacaoUseCaseImpl(
            patrimonioDataProvider,
            new BuscarPatrimoniosPorItemIncorporacaoFiltroConverter(),
            new BuscarPatrimoniosPorItemIncorporacaoOutputDataConverter(),
            sistemaDeArquivosIntegration
        );

        BuscarPatrimoniosPorItemIncorporacaoInputData inputData = BuscarPatrimoniosPorItemIncorporacaoInputData
            .builder()
            .size(15L)
            .page(3L)
            .build();

        useCase.executar(inputData);
    }

    @Test
    public void deveBuscarPatrimoniosPeloItemIncorporacao() {
        BuscarPatrimoniosPorItemIncorporacaoUseCase useCase = new BuscarPatrimoniosPorItemIncorporacaoUseCaseImpl(
            patrimonioDataProvider,
            new BuscarPatrimoniosPorItemIncorporacaoFiltroConverter(),
            new BuscarPatrimoniosPorItemIncorporacaoOutputDataConverter(),
            sistemaDeArquivosIntegration
        );

        BuscarPatrimoniosPorItemIncorporacaoInputData inputData = BuscarPatrimoniosPorItemIncorporacaoInputData
            .builder()
            .itemIncorporacaoId(7L)
            .size(15L)
            .page(3L)
            .build();

        Mockito.when(patrimonioDataProvider.buscarPorFiltro(any(Patrimonio.Filtro.class)))
            .thenReturn(ListaPaginada.<Patrimonio>builder()
                .items(List.of(
                    Patrimonio
                        .builder()
                        .id(1L)
                        .valorLiquido(BigDecimal.valueOf(14900))
                        .valorResidual(BigDecimal.valueOf(12500))
                        .contaContabilClassificacao(ContaContabil.builder().id(5L).build())
                        .build()
                ))
                .totalElements(1L)
                .totalPages(1L)
                .build());

        BuscarPatrimoniosPorItemIncorporacaoOutputData outputData = useCase.executar(inputData);

        Assert.assertTrue(outputData.getItems() instanceof ArrayList);
        Assert.assertEquals(Long.valueOf(1), outputData.getTotalElements());
        Assert.assertEquals(Long.valueOf(1), outputData.getTotalPages());
    }

}
