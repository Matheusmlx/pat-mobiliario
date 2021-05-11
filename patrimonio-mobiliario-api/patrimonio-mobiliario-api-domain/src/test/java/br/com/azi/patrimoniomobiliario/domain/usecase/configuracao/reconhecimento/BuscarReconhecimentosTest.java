package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento;

import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.Reconhecimento;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReconhecimentoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.buscarlistagem.BuscarReconhecimentosInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.buscarlistagem.BuscarReconhecimentosOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.buscarlistagem.BuscarReconhecimentosUsecaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.buscarlistagem.converter.BuscarReconhecimentosFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.buscarlistagem.converter.BuscarReconhecimentosOutputDataConverter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class BuscarReconhecimentosTest {

    @Test
    public void deveBuscarReconhecimentos() {
        ReconhecimentoDataProvider dataProvider = Mockito.mock(ReconhecimentoDataProvider.class);

        BuscarReconhecimentosUsecaseImpl useCase = new BuscarReconhecimentosUsecaseImpl(
            dataProvider,
            new BuscarReconhecimentosOutputDataConverter(),
            new BuscarReconhecimentosFiltroConverter()
        );

        BuscarReconhecimentosInputData inputData = BuscarReconhecimentosInputData
            .builder()
            .page(1L)
            .size(10L)
            .sort("nome")
            .direction("ASC")
            .build();

        Mockito.when(dataProvider.buscarPorFiltro(any(Reconhecimento.Filtro.class)))
            .thenReturn(ListaPaginada.<Reconhecimento>builder()
                .items(List.of(
                    Reconhecimento
                        .builder()
                        .id(1L)
                        .nome("COMPRAS")
                        .situacao(Reconhecimento.Situacao.ATIVO)
                        .execucaoOrcamentaria(true)
                        .empenho(true)
                        .notaFiscal(true)
                        .build()))
                .totalElements(1L)
                .totalPages(1L)
                .build());

        BuscarReconhecimentosOutputData outputData = useCase.executar(inputData);

        Assert.assertTrue(outputData.getItems() instanceof ArrayList);
        Assert.assertFalse(outputData.getItems().isEmpty());
        Assert.assertEquals("COMPRAS", outputData.getItems().get(0).getNome());
        Assert.assertEquals("ATIVO", outputData.getItems().get(0).getSituacao());
        Assert.assertEquals(true, outputData.getItems().get(0).getExecucaoOrcamentaria());
        Assert.assertEquals(java.util.Optional.of(1L).get(), outputData.getItems().get(0).getId());
        Assert.assertEquals(true, outputData.getItems().get(0).getNotaFiscal());
        Assert.assertEquals(true, outputData.getItems().get(0).getEmpenho());
        Assert.assertEquals(java.util.Optional.of(1L).get(), outputData.getTotalElements());
        Assert.assertEquals(java.util.Optional.of(1L).get(), outputData.getTotalPages());

    }

    @Test
    public void deveFalharQuandoNaoTemFiltro() {
        BuscarReconhecimentosInputData buscarPatrimonioInputData = new BuscarReconhecimentosInputData();
        buscarPatrimonioInputData.setSort("ASC");

        try {
            new BuscarReconhecimentosUsecaseImpl(null, null, null).executar(buscarPatrimonioInputData);
        } catch (Exception e) {
            Assert.assertEquals(2, e.getSuppressed().length);
            Assert.assertTrue(e.getSuppressed()[0] instanceof IllegalStateException);
        }
    }
}
