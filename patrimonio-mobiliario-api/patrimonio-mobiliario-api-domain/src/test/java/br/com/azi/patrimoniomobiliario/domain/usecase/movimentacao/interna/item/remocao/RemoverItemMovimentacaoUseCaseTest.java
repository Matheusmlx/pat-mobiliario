package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.remocao;

import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.MovimentacaoItem;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.remocao.exception.MovimentacaoItemNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.remocao.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.remocao.exception.MovimentacaoNaoEstaEmElaboracaoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.remocao.exception.PatrimonioNaoEncontradoException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RemoverItemMovimentacaoUseCaseTest {

    @Mock
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Mock
    private PatrimonioDataProvider patrimonioDataProvider;

    @Mock
    private MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    private RemoverItemMovimentacaoUseCase useCase;

    @Before
    public void iniciliazar() {
        useCase = new RemoverItemMovimentacaoUseCaseImpl(
            movimentacaoDataProvider,
            patrimonioDataProvider,
            movimentacaoItemDataProvider
        );
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoDadosEntradaIncompletos() {
        RemoverItemMovimentacaoInputData inputData = RemoverItemMovimentacaoInputData
            .builder()
            .movimentacaoId(5L)
            .build();

        useCase.executar(inputData);
    }

    @Test(expected = MovimentacaoNaoEncontradaException.class)
    public void deveFalharQuandoMovimentacaoNaoEncontrada() {
        RemoverItemMovimentacaoInputData inputData = RemoverItemMovimentacaoInputData
            .builder()
            .movimentacaoId(5L)
            .patrimonioId(8L)
            .build();

        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.empty());

        useCase.executar(inputData);
    }

    @Test(expected = MovimentacaoNaoEstaEmElaboracaoException.class)
    public void deveFalharQuandoMovimentacaoNaoEstaEmElaboracao() {
        RemoverItemMovimentacaoInputData inputData = RemoverItemMovimentacaoInputData
            .builder()
            .movimentacaoId(5L)
            .patrimonioId(8L)
            .build();

        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(Movimentacao
                .builder()
                .id(3L)
                .situacao(Movimentacao.Situacao.FINALIZADO)
                .build()));

        useCase.executar(inputData);
    }

    @Test(expected = PatrimonioNaoEncontradoException.class)
    public void deveFalharQuandoPatrimonioNaoEncontrado() {
        RemoverItemMovimentacaoInputData inputData = RemoverItemMovimentacaoInputData
            .builder()
            .movimentacaoId(5L)
            .patrimonioId(8L)
            .build();

        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(Movimentacao
                .builder()
                .id(3L)
                .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                .build()));

        Mockito.when(patrimonioDataProvider.existe(anyLong())).thenReturn(false);

        useCase.executar(inputData);
    }

    @Test(expected = MovimentacaoItemNaoEncontradaException.class)
    public void deveFalharQuandoMovimentacaoItemNaoEncontrada() {
        RemoverItemMovimentacaoInputData inputData = RemoverItemMovimentacaoInputData
            .builder()
            .movimentacaoId(5L)
            .patrimonioId(8L)
            .build();

        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(Movimentacao
                .builder()
                .id(3L)
                .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                .build()));

        Mockito.when(patrimonioDataProvider.existe(anyLong())).thenReturn(true);

        Mockito.when(movimentacaoItemDataProvider.buscarPorMovimentacaoEPatrimonio(anyLong(), anyLong())).thenReturn(Optional.empty());

        useCase.executar(inputData);
    }

    @Test
    public void deveRemoverItemMovimentacaoQuandoMovimentacaoEstiverEmElaboracao() {
        RemoverItemMovimentacaoInputData inputData = RemoverItemMovimentacaoInputData
            .builder()
            .movimentacaoId(5L)
            .patrimonioId(8L)
            .build();

        MovimentacaoItem movimentacaoItem = MovimentacaoItem
            .builder()
            .patrimonio(Patrimonio.builder().id(8L).build())
            .movimentacao(Movimentacao.builder().id(5L).build())
            .build();

        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(Movimentacao
                .builder()
                .id(3L)
                .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                .build()));

        Mockito.when(patrimonioDataProvider.existe(anyLong())).thenReturn(true);

        Mockito.when(movimentacaoItemDataProvider.buscarPorMovimentacaoEPatrimonio(anyLong(), anyLong()))
            .thenReturn(
                Optional.of(movimentacaoItem)
            );

        useCase.executar(inputData);

        verify(movimentacaoDataProvider, times(1)).buscarPorId(5L);
        verify(movimentacaoItemDataProvider, times(1)).buscarPorMovimentacaoEPatrimonio(5L, 8L);
        verify(movimentacaoItemDataProvider, times(1)).remover(movimentacaoItem);
    }

    @Test
    public void deveRemoverItemMovimentacaoQuandoMovimentacaoEstiverComErroNoProcessamento() {
        RemoverItemMovimentacaoInputData inputData = RemoverItemMovimentacaoInputData
            .builder()
            .movimentacaoId(5L)
            .patrimonioId(8L)
            .build();

        MovimentacaoItem movimentacaoItem = MovimentacaoItem
            .builder()
            .patrimonio(Patrimonio.builder().id(8L).build())
            .movimentacao(Movimentacao.builder().id(5L).build())
            .build();

        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(Movimentacao
                .builder()
                .id(3L)
                .situacao(Movimentacao.Situacao.ERRO_PROCESSAMENTO)
                .build()));

        Mockito.when(patrimonioDataProvider.existe(anyLong())).thenReturn(true);

        Mockito.when(movimentacaoItemDataProvider.buscarPorMovimentacaoEPatrimonio(anyLong(), anyLong()))
            .thenReturn(
                Optional.of(movimentacaoItem)
            );

        useCase.executar(inputData);

        verify(movimentacaoDataProvider, times(1)).buscarPorId(5L);
        verify(movimentacaoItemDataProvider, times(1)).buscarPorMovimentacaoEPatrimonio(5L, 8L);
        verify(movimentacaoItemDataProvider, times(1)).remover(movimentacaoItem);
    }

}
