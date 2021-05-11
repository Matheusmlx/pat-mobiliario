package br.com.azi.patrimoniomobiliario.domain.usecase.comodante;

import br.com.azi.patrimoniomobiliario.domain.entity.Comodante;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ComodanteDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.comodante.listagem.BuscarComodantesInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.comodante.listagem.BuscarComodantesOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.comodante.listagem.BuscarComodantesUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.comodante.listagem.BuscarComodantesUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.comodante.listagem.converter.BuscarComodantesFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.comodante.listagem.converter.BuscarComodantesOutputDataConverter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BuscarComodantesUseCaseTest {

    @Mock
    private ComodanteDataProvider comodanteDataProvider;

    private BuscarComodantesUseCase useCase;

    @Before
    public void inicializar() {
        useCase = new BuscarComodantesUseCaseImpl(
            comodanteDataProvider,
            new BuscarComodantesOutputDataConverter(),
            new BuscarComodantesFiltroConverter()
        );
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoFiltroIncompleto() {
        BuscarComodantesInputData inputData = BuscarComodantesInputData
            .builder()
            .page(1L)
            .build();

        useCase.executar(inputData);
    }

    @Test
    public void deveBuscarComodantes() {
        BuscarComodantesInputData inputData = BuscarComodantesInputData
            .builder()
            .page(1L)
            .size(100L)
            .build();

        Comodante.Filtro filtro = new Comodante.Filtro();
        filtro.setPage(0L);
        filtro.setSize(100L);

        when(comodanteDataProvider.buscarPorFiltro(any(Comodante.Filtro.class)))
            .thenReturn(
                ListaPaginada.<Comodante>builder()
                .items(
                    List.of(
                        Comodante
                            .builder()
                            .id(1L)
                            .nome("Amanda")
                            .build(),
                        Comodante
                            .builder()
                            .id(2L)
                            .nome("Carlos")
                            .build(),
                        Comodante
                            .builder()
                            .id(3L)
                            .nome("Sophia")
                            .build()
                ))
                .totalElements(3L)
            .build());

        BuscarComodantesOutputData outputData = useCase.executar(inputData);

        Assert.assertEquals(Long.valueOf(3), outputData.getTotalElements());
        Assert.assertEquals(Long.valueOf(1), outputData.getItems().get(0).getId());
        Assert.assertEquals("Amanda", outputData.getItems().get(0).getNome());
        Assert.assertEquals(Long.valueOf(2), outputData.getItems().get(1).getId());
        Assert.assertEquals("Carlos", outputData.getItems().get(1).getNome());
        Assert.assertEquals(Long.valueOf(3), outputData.getItems().get(2).getId());
        Assert.assertEquals("Sophia", outputData.getItems().get(2).getNome());

        verify(comodanteDataProvider, times(1)).buscarPorFiltro(filtro);
    }

}
