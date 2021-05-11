package br.com.azi.patrimoniomobiliario.domain.usecase.tipodocumento.buscar;

import br.com.azi.patrimoniomobiliario.domain.entity.TipoDocumento;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.TipoDocumentoDataprovider;
import br.com.azi.patrimoniomobiliario.domain.usecase.tipodocumento.buscar.converter.BuscarTipoDocumentosOutputDataConverter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class BuscarTipoDocumentosUseCaseTest {

    @Test
    public void deveBuscarTiposDeDocumentos(){
        TipoDocumentoDataprovider tipoDocumentoDataprovider = Mockito.mock(TipoDocumentoDataprovider.class);


        BuscarTipoDocumentosUseCaseImpl useCase = new BuscarTipoDocumentosUseCaseImpl(tipoDocumentoDataprovider,
            new BuscarTipoDocumentosOutputDataConverter());

        Mockito.when(tipoDocumentoDataprovider.buscar())
            .thenReturn(List.of(TipoDocumento.builder()
                .id(1L)
                .descricao("Nota Fiscal")
                .build()));

        BuscarTipoDocumentosOutputData outputData = useCase.executar();

        Assert.assertTrue(outputData.getItems() instanceof ArrayList);
        Assert.assertEquals(1, outputData.getItems().size());
        Assert.assertEquals(Long.valueOf(1), outputData.getItems().get(0).getId());
        Assert.assertEquals("Nota Fiscal", outputData.getItems().get(0).getDescricao());
    }
}
