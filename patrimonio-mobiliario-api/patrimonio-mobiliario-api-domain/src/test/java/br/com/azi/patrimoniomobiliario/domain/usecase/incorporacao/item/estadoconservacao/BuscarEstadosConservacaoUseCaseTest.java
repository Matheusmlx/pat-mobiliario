package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.estadoconservacao;

import br.com.azi.patrimoniomobiliario.domain.entity.EstadoConservacao;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.EstadoConservacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.estadoconservacao.listagem.BuscarEstadosConservacaoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.estadoconservacao.listagem.BuscarEstadosConservacaoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.estadoconservacao.listagem.BuscarEstadosConservacaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.estadoconservacao.listagem.converter.EstadoConservacaoOutputDataConverter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class BuscarEstadosConservacaoUseCaseTest {

    private EstadoConservacaoDataProvider estadoConservacaoDataProvider;

    @Before
    public void setUp() {
        estadoConservacaoDataProvider = Mockito.mock(EstadoConservacaoDataProvider.class);
    }

    @Test
    public void deveBuscarEstadosConservacao() {
        BuscarEstadosConservacaoUseCase useCase = new BuscarEstadosConservacaoUseCaseImpl(
            estadoConservacaoDataProvider,
            new EstadoConservacaoOutputDataConverter()
        );

        Mockito.when(estadoConservacaoDataProvider.buscarEstadosConservacao())
            .thenReturn(List.of(
                EstadoConservacao
                    .builder()
                    .id(1L)
                    .nome("Ótimo")
                    .prioridade(1)
                    .build(),
                EstadoConservacao
                    .builder()
                    .id(2L)
                    .nome("Bom")
                    .prioridade(2)
                    .build()
                ));

        BuscarEstadosConservacaoOutputData outputData = useCase.executar();

        assertEquals(2, outputData.getEstadosConservacao().size());
        assertEquals(Long.valueOf(1), outputData.getEstadosConservacao().get(0).getId());
        assertEquals("Ótimo", outputData.getEstadosConservacao().get(0).getNome());
        assertEquals(Integer.valueOf(1), outputData.getEstadosConservacao().get(0).getPrioridade());
        assertEquals("Bom", outputData.getEstadosConservacao().get(1).getNome());
        assertEquals(Integer.valueOf(2), outputData.getEstadosConservacao().get(1).getPrioridade());
    }
}
