package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemporincorporacao.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.ItemIncorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemporincorporacao.BuscarPatrimoniosIncorporacaoOutputData;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class BuscarPatrimoniosIncorporacaoOutputDataConverterTest {

    @Test
    public void deveConverterEntidades() {
        BuscarPatrimoniosIncorporacaoOutputDataConverter converter = new BuscarPatrimoniosIncorporacaoOutputDataConverter();

        BuscarPatrimoniosIncorporacaoOutputData buscarPatrimoniosIncorporacaoOutputData = BuscarPatrimoniosIncorporacaoOutputData
            .builder()
            .items(
                List.of(
                    BuscarPatrimoniosIncorporacaoOutputData.Item
                        .builder()
                        .numero("00000001")
                        .descricao("item incorporação 1")
                        .valor(BigDecimal.valueOf(350))
                        .build(),
                    BuscarPatrimoniosIncorporacaoOutputData.Item
                        .builder()
                        .numero("00000002")
                        .descricao("item incorporação 2")
                        .valor(BigDecimal.valueOf(153))
                        .build(),
                    BuscarPatrimoniosIncorporacaoOutputData.Item
                        .builder()
                        .numero("00000003")
                        .descricao("item incorporação 3")
                        .valor(BigDecimal.valueOf(500))
                        .build()))
            .totalElements(3L)
            .totalPages(1L)
            .totalElementsOfAllPages(5L)
            .build();

        ListaPaginada<Patrimonio> listaPaginada = ListaPaginada
            .<Patrimonio>builder()
            .items(
                List.of(
                    Patrimonio
                        .builder()
                        .id(1L)
                        .numero("00000001")
                        .itemIncorporacao(
                            ItemIncorporacao
                            .builder()
                            .codigo("asdf1234")
                            .descricao("item incorporação 1")
                            .build())
                        .valorAquisicao(BigDecimal.valueOf(350))
                        .build(),
                    Patrimonio
                        .builder()
                        .id(1L)
                        .numero("00000002")
                        .itemIncorporacao(
                            ItemIncorporacao
                                .builder()
                                .codigo("asdf1235")
                                .descricao("item incorporação 2")
                                .build())
                        .valorAquisicao(BigDecimal.valueOf(153))
                        .build(),
                    Patrimonio
                        .builder()
                        .id(1L)
                        .numero("00000003")
                        .itemIncorporacao(
                            ItemIncorporacao
                                .builder()
                                .codigo("asdf1236")
                                .descricao("item incorporação 3")
                                .build())
                        .valorAquisicao(BigDecimal.valueOf(500))
                        .build()))
            .totalElements(3L)
            .totalPages(1L)
            .build();

        BuscarPatrimoniosIncorporacaoOutputData outputData = converter.to(listaPaginada, 5L);

        Assert.assertEquals(buscarPatrimoniosIncorporacaoOutputData.getItems().get(0).getNumero(), outputData.getItems().get(0).getNumero());
        Assert.assertEquals(buscarPatrimoniosIncorporacaoOutputData.getItems().get(0).getDescricao(), outputData.getItems().get(0).getDescricao());
        Assert.assertEquals(buscarPatrimoniosIncorporacaoOutputData.getItems().get(0).getValor(), outputData.getItems().get(0).getValor());

        Assert.assertEquals(buscarPatrimoniosIncorporacaoOutputData.getItems().get(1).getNumero(), outputData.getItems().get(1).getNumero());
        Assert.assertEquals(buscarPatrimoniosIncorporacaoOutputData.getItems().get(1).getDescricao(), outputData.getItems().get(1).getDescricao());
        Assert.assertEquals(buscarPatrimoniosIncorporacaoOutputData.getItems().get(1).getValor(), outputData.getItems().get(1).getValor());

        Assert.assertEquals(buscarPatrimoniosIncorporacaoOutputData.getItems().get(2).getNumero(), outputData.getItems().get(2).getNumero());
        Assert.assertEquals(buscarPatrimoniosIncorporacaoOutputData.getItems().get(2).getDescricao(), outputData.getItems().get(2).getDescricao());
        Assert.assertEquals(buscarPatrimoniosIncorporacaoOutputData.getItems().get(2).getValor(), outputData.getItems().get(2).getValor());

        Assert.assertEquals(buscarPatrimoniosIncorporacaoOutputData.getTotalElements(), outputData.getTotalElements());
        Assert.assertEquals(buscarPatrimoniosIncorporacaoOutputData.getTotalPages(), outputData.getTotalPages());
        Assert.assertEquals(buscarPatrimoniosIncorporacaoOutputData.getTotalElementsOfAllPages(), outputData.getTotalElementsOfAllPages());
    }

}
