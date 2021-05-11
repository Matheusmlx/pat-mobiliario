package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.buscarlistagem;

import br.com.azi.patrimoniomobiliario.domain.entity.Concedente;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ConcedenteDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.buscarlistagem.converter.BuscarConcedentesFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.buscarlistagem.converter.BuscarConcedentesOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.buscarlistagem.exception.FiltroIncompletoException;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

public class BuscarConcedentesUseCaseTest {

    @Test
    public void deveBuscarConcedentes() {
        ConcedenteDataProvider dataProvider = Mockito.mock(ConcedenteDataProvider.class);

        BuscarConcedentesUseCaseImpl useCase = new BuscarConcedentesUseCaseImpl(
            dataProvider,
            new BuscarConcedentesOutputDataConverter(),
            new BuscarConcedentesFiltroConverter()
        );

        BuscarConcedentesInputData inputData = BuscarConcedentesInputData
            .builder()
            .page(1L)
            .size(10L)
            .sort("nome")
            .direction("ASC")
            .build();

        Mockito.when(dataProvider.buscarPorFiltro(any(Concedente.Filtro.class)))
            .thenReturn(ListaPaginada.<Concedente>builder()
                .items(List.of(
                    Concedente
                        .builder()
                        .id(1L)
                        .cpfCnpj("438.018.270-38")
                        .tipoPessoa(Concedente.Pessoa.FISICA)
                        .nome("Mário de Freitas")
                        .situacao(Concedente.Situacao.ATIVO)
                        .inclusaoSistema(true)
                        .build()))
                .totalElements(1L)
                .totalPages(1L)
                    .build());

        BuscarConcedentesOutputData outputData = useCase.executar(inputData);

        Assert.assertTrue(outputData.getItems() instanceof ArrayList);
        Assert.assertFalse(outputData.getItems().isEmpty());
        Assert.assertEquals("438.018.270-38", outputData.getItems().get(0).getCpfCnpj());
        Assert.assertEquals("Mário de Freitas", outputData.getItems().get(0).getNome());
        Assert.assertEquals("ATIVO", outputData.getItems().get(0).getSituacao());
        Assert.assertEquals(java.util.Optional.of(1L).get(), outputData.getItems().get(0).getId());
        Assert.assertEquals(java.util.Optional.of(1L).get(), outputData.getTotalElements());
        Assert.assertEquals(java.util.Optional.of(1L).get(), outputData.getTotalPages());

    }

    @Test
    public void deveFalharQuandoNaoTemFiltro() {
        BuscarConcedentesInputData buscarPatrimonioInputData = new BuscarConcedentesInputData();
        buscarPatrimonioInputData.setSort("ASC");

        try {
            new BuscarConcedentesUseCaseImpl(null,  null, null).executar(buscarPatrimonioInputData);
        } catch (Exception e) {
            Assert.assertEquals(2, e.getSuppressed().length);
            Assert.assertTrue(e.getSuppressed()[0] instanceof FiltroIncompletoException);
        }
    }
}
