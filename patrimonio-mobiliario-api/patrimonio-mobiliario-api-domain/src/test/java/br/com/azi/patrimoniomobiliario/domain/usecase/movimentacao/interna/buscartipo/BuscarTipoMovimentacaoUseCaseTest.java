package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.buscartipo;

import br.com.azi.patrimoniomobiliario.domain.constant.movimentacao.TipoMovimentacaoEnum;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.buscartipo.converter.BuscarTipoMovimentacaoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.buscartipo.exception.MovimentacaoNaoEncontradaException;
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
public class BuscarTipoMovimentacaoUseCaseTest {

    @Mock
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoIdMovimentacaoEhNulo() {
        BuscarTipoMovimentacaoUseCase useCase = new BuscarTipoMovimentacaoUseCaseImpl(
            movimentacaoDataProvider,
            new BuscarTipoMovimentacaoOutputDataConverter()
        );

        BuscarTipoMovimentacaoInputData inputData = BuscarTipoMovimentacaoInputData.builder().build();

        useCase.executar(inputData);
    }

    @Test(expected = MovimentacaoNaoEncontradaException.class)
    public void deveFalharQuandoMovimentacaoNaoEncontrada() {
        BuscarTipoMovimentacaoUseCase useCase = new BuscarTipoMovimentacaoUseCaseImpl(
            movimentacaoDataProvider,
            new BuscarTipoMovimentacaoOutputDataConverter()
        );

        BuscarTipoMovimentacaoInputData inputData = BuscarTipoMovimentacaoInputData
            .builder()
            .movimentacaoId(10L)
            .build();

        Mockito.when(movimentacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.empty());

        useCase.executar(inputData);

        verify(movimentacaoDataProvider, times(1)).buscarPorId(10L);
    }

    @Test
    public void deveBuscarTipoMovimentacao() {
        BuscarTipoMovimentacaoUseCase useCase = new BuscarTipoMovimentacaoUseCaseImpl(
            movimentacaoDataProvider,
            new BuscarTipoMovimentacaoOutputDataConverter()
        );

        BuscarTipoMovimentacaoInputData inputData = BuscarTipoMovimentacaoInputData
            .builder()
            .movimentacaoId(10L)
            .build();

        Mockito.when(movimentacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Movimentacao
                .builder()
                .tipo(TipoMovimentacaoEnum.DISTRIBUICAO)
                .build()));

        BuscarTipoMovimentacaoOutputData outputData = useCase.executar(inputData);

        verify(movimentacaoDataProvider, times(1)).buscarPorId(10L);

        assertEquals("DISTRIBUICAO", outputData.getTipo());
    }

}
