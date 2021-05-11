package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.remocao.vazia;

import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.remocao.vazia.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.remocao.vazia.exception.MovimentacaoNaoEstaEmElaboracaoException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RemoverMovimentacaoInternaVaziaUseCaseTest {

    @Mock
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoNaoTiverIdMovimentacao() {
        RemoverMovimentacaoInternaVaziaUseCase useCase = new RemoverMovimentacaoInternaVaziaUseCaseImpl(
            movimentacaoDataProvider
        );

        RemoverMovimentacaoInternaVaziaInputData inputData = RemoverMovimentacaoInternaVaziaInputData.builder().build();

        useCase.executar(inputData);
    }

    @Test(expected = MovimentacaoNaoEncontradaException.class)
    public void deveFalharQuandoMovimentacaoNaoEncontrada() {
        RemoverMovimentacaoInternaVaziaUseCase useCase = new RemoverMovimentacaoInternaVaziaUseCaseImpl(
            movimentacaoDataProvider
        );

        RemoverMovimentacaoInternaVaziaInputData inputData = RemoverMovimentacaoInternaVaziaInputData
            .builder()
            .movimentacaoId(1L)
            .build();

        Mockito.when(movimentacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.empty());

        useCase.executar(inputData);
    }

    @Test(expected = MovimentacaoNaoEstaEmElaboracaoException.class)
    public void deveFalharQuandoMovimentacaoNaoEstiverEmElaboracao() {
        RemoverMovimentacaoInternaVaziaUseCase useCase = new RemoverMovimentacaoInternaVaziaUseCaseImpl(
            movimentacaoDataProvider
        );

        RemoverMovimentacaoInternaVaziaInputData inputData = RemoverMovimentacaoInternaVaziaInputData
            .builder()
            .movimentacaoId(1L)
            .build();

        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(Movimentacao.builder()
                .id(1L)
                .situacao(Movimentacao.Situacao.FINALIZADO)
                .build()));

        useCase.executar(inputData);
    }

    @Test
    public void deveRemoverMovimentacao() {
        RemoverMovimentacaoInternaVaziaUseCase useCase = new RemoverMovimentacaoInternaVaziaUseCaseImpl(
            movimentacaoDataProvider
        );

        RemoverMovimentacaoInternaVaziaInputData inputData = RemoverMovimentacaoInternaVaziaInputData
            .builder()
            .movimentacaoId(1L)
            .build();

        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(
                Movimentacao.builder()
                    .id(1L)
                    .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                    .build()
            ));

        useCase.executar(inputData);

        verify(movimentacaoDataProvider, times(1)).removerPorId(1L);
    }

}
