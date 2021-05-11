package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.buscarquantidadeitens.BuscarQuantidadeItensMovimentacaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.buscarquantidadeitens.BuscarQuantidadeItensMovimentacaoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.buscarquantidadeitens.BuscarQuantidadeItensMovimentacaoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.buscarquantidadeitens.BuscarQuantidadeItensMovimentacaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.buscarquantidadeitens.converter.BuscarQuantidadeItensMovimentacaoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.buscarquantidadeitens.exception.MovimentacaoNaoEncontradaException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BuscarQuantidadeItensUseCaseTest {

    @Mock
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Mock
    private MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    private BuscarQuantidadeItensMovimentacaoUseCase useCase;

    @Before
    public void inicializar() {
        useCase = new BuscarQuantidadeItensMovimentacaoUseCaseImpl(
            movimentacaoDataProvider,
            movimentacaoItemDataProvider,
            new BuscarQuantidadeItensMovimentacaoOutputDataConverter()
        );
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoMovimentacaoIdForNulo() {
        useCase.executar(new BuscarQuantidadeItensMovimentacaoInputData());
    }

    @Test(expected = MovimentacaoNaoEncontradaException.class)
    public void deveFalharQuandoMovimentacaoNaoForEncontrada() {
        final Long movimentacaoId = 1L;

        when(movimentacaoDataProvider.existe(movimentacaoId)).thenReturn(false);

        useCase.executar(new BuscarQuantidadeItensMovimentacaoInputData(movimentacaoId));
    }

    @Test
    public void deveRetornarAQuantidadeDeItensAdicionadosNaMovimentacao() {
        final Long movimentacaoId = 1L;

        final BuscarQuantidadeItensMovimentacaoOutputData outputDataEsperado = BuscarQuantidadeItensMovimentacaoOutputData
            .builder()
            .movimentacaoId(movimentacaoId)
            .quantidadeItens(10L)
            .build();

        when(movimentacaoDataProvider.existe(movimentacaoId)).thenReturn(true);
        when(movimentacaoItemDataProvider.buscarQuantidadeItensPorMovimentacaoId(movimentacaoId)).thenReturn(10L);

        final BuscarQuantidadeItensMovimentacaoInputData inputData = new BuscarQuantidadeItensMovimentacaoInputData(movimentacaoId);
        final BuscarQuantidadeItensMovimentacaoOutputData outputData = useCase.executar(inputData);

        assertEquals(outputDataEsperado, outputData);
    }
}
