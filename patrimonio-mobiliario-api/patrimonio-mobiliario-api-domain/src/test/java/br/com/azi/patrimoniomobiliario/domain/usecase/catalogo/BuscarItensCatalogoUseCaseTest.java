package br.com.azi.patrimoniomobiliario.domain.usecase.catalogo;

import br.com.azi.patrimoniomobiliario.domain.entity.ItemCatalogo;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ItemCatalogoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.catalogo.buscarlistagem.BuscarItensCatalogoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.catalogo.buscarlistagem.BuscarItensCatalogoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.catalogo.buscarlistagem.BuscarItensCatalogoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.catalogo.buscarlistagem.converter.BuscarItensCatalogoFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.catalogo.buscarlistagem.converter.BuscarItensCatalogoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.catalogo.buscarlistagem.exception.FiltroIncompletoException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

public class BuscarItensCatalogoUseCaseTest {


    private ItemCatalogoDataProvider itemCatalogoDataProvider;

    @Before
    public void inicializa(){ itemCatalogoDataProvider = Mockito.mock(ItemCatalogoDataProvider.class);}

    @Test
    public void deveBuscarTodosItensCatalogo(){
        BuscarItensCatalogoUseCaseImpl useCase = new BuscarItensCatalogoUseCaseImpl(
            itemCatalogoDataProvider,
            new BuscarItensCatalogoFiltroConverter(),
            new BuscarItensCatalogoOutputDataConverter());

        BuscarItensCatalogoInputData inputData = BuscarItensCatalogoInputData
            .builder()
            .page(1L)
            .size(10L)
            .sort("codigo")
            .direction("ASC")
            .build();

        Mockito.when(itemCatalogoDataProvider.buscarPorFiltro(any(ItemCatalogo.Filtro.class)))
            .thenReturn(ListaPaginada.<ItemCatalogo>builder()
                .items(List.of(
                    ItemCatalogo
                        .builder()
                        .id(1L)
                        .codigo("123456")
                        .descricao("descricao 1")
                        .materialServicoId(1L)
                        .situacao("ATIVO")
                        .status("APROVADO")
                        .tipo("MATERIAL")
                        .build(),
                    ItemCatalogo
                        .builder()
                        .id(2L)
                        .codigo("234567")
                        .descricao("descricao 2")
                        .materialServicoId(2L)
                        .situacao("ATIVO")
                        .status("APROVADO")
                        .tipo("MATERIAL")
                        .build()
                ))
                .totalElements(2L)
                .totalPages(1L)
                .build());

        BuscarItensCatalogoOutputData outputData = useCase.executar(inputData);

        Assert.assertTrue(outputData.getItems() instanceof ArrayList);
        Assert.assertFalse(outputData.getItems().isEmpty());
        Assert.assertEquals(java.util.Optional.of(1L).get(), outputData.getItems().get(0).getId());
        Assert.assertEquals("123456", outputData.getItems().get(0).getCodigo());
        Assert.assertEquals("descricao 1", outputData.getItems().get(0).getDescricao());
        Assert.assertEquals(java.util.Optional.of(2L).get(), outputData.getItems().get(1).getId());
        Assert.assertEquals("234567", outputData.getItems().get(1).getCodigo());
        Assert.assertEquals("descricao 2", outputData.getItems().get(1).getDescricao());
        Assert.assertEquals(java.util.Optional.of(2L).get(), outputData.getTotalElements());
        Assert.assertEquals(java.util.Optional.of(1L).get(), outputData.getTotalPages());
    }

    @Test
    public void deveFalharQuandoNaoTemFiltro() {
        BuscarItensCatalogoInputData inputData = new BuscarItensCatalogoInputData();
        inputData.setSort("ASC");

        try {
            new BuscarItensCatalogoUseCaseImpl(null,  null, null).executar(inputData);
        } catch (Exception e) {
            Assert.assertEquals(2, e.getSuppressed().length);
            Assert.assertTrue(e.getSuppressed()[0] instanceof FiltroIncompletoException);
        }
    }
}
