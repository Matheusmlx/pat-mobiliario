package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenio.buscarlistagem;

import br.com.azi.patrimoniomobiliario.domain.entity.Concedente;
import br.com.azi.patrimoniomobiliario.domain.entity.Convenio;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ConvenioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.buscarlistagem.BuscarConveniosInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.buscarlistagem.BuscarConveniosOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.buscarlistagem.BuscarConveniosUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.buscarlistagem.converter.BuscarConveniosFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.buscarlistagem.converter.BuscarConveniosOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.buscarlistagem.exception.FiltroIncompletoException;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

public class BuscarConveniosUseCaseTest {

    @Test
    public void deveBuscarConvenios() {
        ConvenioDataProvider dataProvider = Mockito.mock(ConvenioDataProvider.class);

        BuscarConveniosUseCaseImpl useCase = new BuscarConveniosUseCaseImpl(
            dataProvider,
            new BuscarConveniosOutputDataConverter(),
            new BuscarConveniosFiltroConverter()
        );

        BuscarConveniosInputData inputData = BuscarConveniosInputData
            .builder()
            .page(1L)
            .size(10L)
            .sort("nome")
            .direction("ASC")
            .build();

        Mockito.when(dataProvider.buscarPorFiltro(any(Convenio.Filtro.class)))
            .thenReturn(ListaPaginada.<Convenio>builder()
                .items(List.of(
                    Convenio
                        .builder()
                        .id(1L)
                        .numero("12345678")
                        .concedente(Concedente.builder()
                            .id(1L)
                            .nome("concedente")
                            .build())
                        .nome("Mário de Freitas")
                        .situacao(Convenio.Situacao.ATIVO)
                        .fonteRecurso("fonte")
                        .build()))
                .totalElements(1L)
                .totalPages(1L)
                    .build());

        BuscarConveniosOutputData outputData = useCase.executar(inputData);

        Assert.assertTrue(outputData.getItems() instanceof ArrayList);
        Assert.assertFalse(outputData.getItems().isEmpty());
        Assert.assertEquals("12345678", outputData.getItems().get(0).getNumero());
        Assert.assertEquals("Mário de Freitas", outputData.getItems().get(0).getNome());
        Assert.assertEquals("ATIVO", outputData.getItems().get(0).getSituacao());
        Assert.assertEquals(java.util.Optional.of(1L).get(), outputData.getItems().get(0).getId());
        Assert.assertEquals("fonte", outputData.getItems().get(0).getFonteRecurso());
        Assert.assertEquals(java.util.Optional.of(1L).get(), outputData.getTotalElements());
        Assert.assertEquals(java.util.Optional.of(1L).get(), outputData.getTotalPages());
        Assert.assertEquals(java.util.Optional.of(1L).get(), outputData.getItems().get(0).getConcedente().getId());
        Assert.assertEquals("concedente", outputData.getItems().get(0).getConcedente().getNome());
    }

    @Test
    public void deveFalharQuandoNaoTemFiltro() {
        BuscarConveniosInputData buscarPatrimonioInputData = new BuscarConveniosInputData();
        buscarPatrimonioInputData.setSort("ASC");

        try {
            new BuscarConveniosUseCaseImpl(null,  null, null).executar(buscarPatrimonioInputData);
        } catch (Exception e) {
            Assert.assertEquals(2, e.getSuppressed().length);
            Assert.assertTrue(e.getSuppressed()[0] instanceof FiltroIncompletoException);
        }
    }
}
