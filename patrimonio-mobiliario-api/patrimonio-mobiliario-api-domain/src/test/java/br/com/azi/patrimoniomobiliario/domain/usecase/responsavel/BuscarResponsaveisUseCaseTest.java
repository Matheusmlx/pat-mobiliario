package br.com.azi.patrimoniomobiliario.domain.usecase.responsavel;

import br.com.azi.patrimoniomobiliario.domain.entity.Responsavel;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ResponsavelDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.responsavel.listagem.BuscarResponsaveisOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.responsavel.listagem.BuscarResponsaveisUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.responsavel.listagem.BuscarResponsaveisUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.responsavel.listagem.converter.BuscarResponsaveisOutputDataConverter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class BuscarResponsaveisUseCaseTest {

    @Mock
    private ResponsavelDataProvider responsavelDataProvider;

    private BuscarResponsaveisUseCase useCase;

    @Before
    public void inicializar() {
        useCase = new BuscarResponsaveisUseCaseImpl(
            responsavelDataProvider,
            new BuscarResponsaveisOutputDataConverter()
        );
    }

    @Test
    public void deveBuscarResponsaveis() {
        Mockito.when(responsavelDataProvider.buscarResponsaveis())
            .thenReturn(
                List.of(
                    Responsavel
                        .builder()
                        .id(1L)
                        .nome("Letícia")
                        .build(),
                    Responsavel
                        .builder()
                        .id(2L)
                        .nome("Thiago")
                        .build(),
                    Responsavel
                        .builder()
                        .id(3L)
                        .nome("Thaís")
                        .build()
                    ));

        BuscarResponsaveisOutputData outputData = useCase.executar();

        Assert.assertEquals(3, outputData.getItems().size());
        Assert.assertEquals(Long.valueOf(1), outputData.getItems().get(0).getId());
        Assert.assertEquals("Letícia", outputData.getItems().get(0).getNome());
        Assert.assertEquals(Long.valueOf(2), outputData.getItems().get(1).getId());
        Assert.assertEquals("Thiago", outputData.getItems().get(1).getNome());
        Assert.assertEquals(Long.valueOf(3), outputData.getItems().get(2).getId());
        Assert.assertEquals("Thaís", outputData.getItems().get(2).getNome());

    }


}
