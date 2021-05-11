package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria;

import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.visualizacao.BuscarInformacaoDevolucaoPatrimoniosInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.visualizacao.BuscarInformacaoDevolucaoPatrimoniosOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.visualizacao.BuscarInformacaoDevolucaoPatrimoniosUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.visualizacao.BuscarInformacaoDevolucaoPatrimoniosUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.visualizacao.converter.BuscarInformacaoDevolucaoPatrimoniosOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.visualizacao.exception.MovimentacaoNaoEncontrada;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.visualizacao.exception.SituacaoMovimentacaoException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BuscarInformacaoDevolucaoPatrimoniosUseCaseTest {

    @Mock
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Mock
    private MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    private BuscarInformacaoDevolucaoPatrimoniosUseCase useCase;

    @Before
    public void inicializar() {
        useCase = new BuscarInformacaoDevolucaoPatrimoniosUseCaseImpl(
            movimentacaoDataProvider,
            movimentacaoItemDataProvider,
            new BuscarInformacaoDevolucaoPatrimoniosOutputDataConverter()
        );
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoIdMovimentacaoNulo() {
        BuscarInformacaoDevolucaoPatrimoniosInputData inputData = new BuscarInformacaoDevolucaoPatrimoniosInputData();

        useCase.executar(inputData);
    }

    @Test(expected = MovimentacaoNaoEncontrada.class)
    public void deveFalharQuandoMovimentacaoNaoEncontrada() {
        BuscarInformacaoDevolucaoPatrimoniosInputData inputData = new BuscarInformacaoDevolucaoPatrimoniosInputData(5L);

        when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.empty());

        useCase.executar(inputData);
    }

    @Test(expected = SituacaoMovimentacaoException.class)
    public void deveFalharQuandoMovimentacaoNaoEstaDevolvidaParcialmente() {
        BuscarInformacaoDevolucaoPatrimoniosInputData inputData = new BuscarInformacaoDevolucaoPatrimoniosInputData(5L);

        when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(
                Movimentacao
                    .builder()
                    .id(5L)
                    .situacao(Movimentacao.Situacao.AGUARDANDO_DEVOLUCAO)
                    .build()
            ));

        useCase.executar(inputData);
    }

    @Test
    public void deveBuscarInformacaoPatrimoniosDevolvidos() {
        BuscarInformacaoDevolucaoPatrimoniosInputData inputData = new BuscarInformacaoDevolucaoPatrimoniosInputData(5L);

        when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(
                Movimentacao
                    .builder()
                    .id(5L)
                    .situacao(Movimentacao.Situacao.DEVOLVIDO_PARCIAL)
                    .build()
            ));

        when(movimentacaoItemDataProvider.buscarQuantidadeItensDevolvidos(anyLong()))
            .thenReturn(7L);

        when(movimentacaoItemDataProvider.buscarQuantidadeItensPorMovimentacaoId(anyLong()))
            .thenReturn(12L);

        BuscarInformacaoDevolucaoPatrimoniosOutputData outputData = useCase.executar(inputData);

        assertEquals("7/12 patrimônios devolvidos", outputData.getRazaoPatrimoniosDevolvidos());
    }
}
