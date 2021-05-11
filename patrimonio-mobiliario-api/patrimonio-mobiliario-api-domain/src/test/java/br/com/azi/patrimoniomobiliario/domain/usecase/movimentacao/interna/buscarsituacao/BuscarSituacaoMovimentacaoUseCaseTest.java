package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.buscarsituacao;

import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.buscarsituacao.converter.BuscarSitucacaoMovimentacaoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.buscarsituacao.exception.MovimentacaoNaoEncontradaException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class BuscarSituacaoMovimentacaoUseCaseTest {

    @Mock
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Test
    public void deveBuscarSituacaoMovimentacao() {
        BuscarSituacaoMovimentacaoUseCase useCase = new BuscarSituacaoMovimentacaoUseCaseImpl(
            movimentacaoDataProvider,
            new BuscarSitucacaoMovimentacaoOutputDataConverter()
        );

        BuscarSituacaoMovimentacaoInputData inputData = BuscarSituacaoMovimentacaoInputData
            .builder()
            .movimentacaoId(10L)
            .build();

        Mockito.when(movimentacaoDataProvider.existe(any(Long.class))).thenReturn(true);

        Mockito.when(movimentacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Movimentacao
                .builder()
                .situacao(Movimentacao.Situacao.EM_PROCESSAMENTO)
                .build()));

        BuscarSituacaoMovimentacaoOutputData outputData = useCase.executar(inputData);

        verify(movimentacaoDataProvider, times(1)).buscarPorId(10L);

        assertEquals("EM_PROCESSAMENTO", outputData.getSituacao());
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoIdMovimentacaoEhNulo() {
        BuscarSituacaoMovimentacaoUseCase useCase = new BuscarSituacaoMovimentacaoUseCaseImpl(
            movimentacaoDataProvider,
            new BuscarSitucacaoMovimentacaoOutputDataConverter()
        );

        BuscarSituacaoMovimentacaoInputData inputData = BuscarSituacaoMovimentacaoInputData.builder().build();

        useCase.executar(inputData);
    }

    @Test(expected = MovimentacaoNaoEncontradaException.class)
    public void deveFalharQuandoMovimentacaoNaoEncontrada() {
        BuscarSituacaoMovimentacaoUseCase useCase = new BuscarSituacaoMovimentacaoUseCaseImpl(
            movimentacaoDataProvider,
            new BuscarSitucacaoMovimentacaoOutputDataConverter()
        );

        BuscarSituacaoMovimentacaoInputData inputData = BuscarSituacaoMovimentacaoInputData
            .builder()
            .movimentacaoId(10L)
            .build();

        Mockito.when(movimentacaoDataProvider.existe(any(Long.class))).thenReturn(false);

        useCase.executar(inputData);

        verify(movimentacaoDataProvider, times(1)).buscarPorId(10L);
    }
}
