package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemporincorporacao;

import br.com.azi.patrimoniomobiliario.domain.entity.ItemIncorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ItemIncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemporincorporacao.converter.BuscarPatrimoniosIncorporacaoFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemporincorporacao.converter.BuscarPatrimoniosIncorporacaoOutputDataConverter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class BuscarPatrimoniosIncorporacaoUseCaseTest {

    private ItemIncorporacaoDataProvider itemIncorporacaoDataProvider;

    private PatrimonioDataProvider patrimonioDataProvider;

    @Before
    public void setUp() {
        itemIncorporacaoDataProvider = Mockito.mock(ItemIncorporacaoDataProvider.class);
        patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoFiltroIncompleto() {
        BuscarPatrimoniosIncorporacaoUseCase useCase = new BuscarPatrimoniosIncorporacaoUseCaseImpl(
            itemIncorporacaoDataProvider,
            patrimonioDataProvider,
            new BuscarPatrimoniosIncorporacaoOutputDataConverter(),
            new BuscarPatrimoniosIncorporacaoFiltroConverter()
        );

        BuscarPatrimoniosIncorporacaoInputData inputData = BuscarPatrimoniosIncorporacaoInputData
            .builder()
            .page(1L)
            .size(10L)
            .build();

        useCase.executar(inputData);
    }

    @Test
    public void deveBuscarPatrimoniosIncorporacao() {
        BuscarPatrimoniosIncorporacaoUseCase useCase = new BuscarPatrimoniosIncorporacaoUseCaseImpl(
            itemIncorporacaoDataProvider,
            patrimonioDataProvider,
            new BuscarPatrimoniosIncorporacaoOutputDataConverter(),
            new BuscarPatrimoniosIncorporacaoFiltroConverter()
        );

        BuscarPatrimoniosIncorporacaoInputData inputData = BuscarPatrimoniosIncorporacaoInputData
            .builder()
            .incorporacaoId(3L)
            .page(1L)
            .size(10L)
            .build();

        Mockito.when(itemIncorporacaoDataProvider.buscarItensPorIncorporacaoId(any(Long.class)))
            .thenReturn(
                List.of(
                    ItemIncorporacao
                        .builder()
                        .id(1L)
                        .build(),
                    ItemIncorporacao
                        .builder()
                        .id(2L)
                        .build(),
                    ItemIncorporacao
                        .builder()
                        .id(10L)
                        .build()));

        Mockito.when(patrimonioDataProvider.buscarPatrimoniosPorItensIncorporacao(any(Patrimonio.Filtro.class)))
            .thenReturn(ListaPaginada
                .<Patrimonio>builder()
                .items(
                    List.of(
                        Patrimonio
                            .builder()
                            .id(1L)
                            .numero("00000001")
                            .valorAquisicao(BigDecimal.valueOf(350))
                            .itemIncorporacao(
                                ItemIncorporacao
                                    .builder()
                                    .codigo("asdf1234")
                                    .descricao("item incorporação 1")
                                    .build())
                            .build(),
                        Patrimonio
                            .builder()
                            .id(2L)
                            .numero("00000002")
                            .valorAquisicao(BigDecimal.valueOf(153))
                            .itemIncorporacao(
                                ItemIncorporacao
                                    .builder()
                                    .codigo("asdf1235")
                                    .descricao("item incorporação 2")
                                    .build())
                            .build(),
                        Patrimonio
                            .builder()
                            .id(3L)
                            .numero("00000002")
                            .valorAquisicao(BigDecimal.valueOf(500))
                            .itemIncorporacao(
                                ItemIncorporacao
                                    .builder()
                                    .codigo("asdf1236")
                                    .descricao("item incorporação 3")
                                    .build())
                            .build()
                        )
                )
                .totalPages(1L)
                .totalElements(3L)
                .build());


        BuscarPatrimoniosIncorporacaoOutputData outputData = useCase.executar(inputData);

        Assert.assertEquals(Long.valueOf(3L), outputData.getTotalElements());
        Assert.assertEquals(Long.valueOf(1L), outputData.getTotalPages());
        Assert.assertEquals(Long.valueOf(1L), outputData.getItems().get(0).getId());
        Assert.assertEquals("00000001", outputData.getItems().get(0).getNumero());
        Assert.assertEquals("item incorporação 1", outputData.getItems().get(0).getDescricao());
        Assert.assertEquals(BigDecimal.valueOf(350), outputData.getItems().get(0).getValor());
    }

}
