package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item;

import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.ItemIncorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.MovimentacaoItem;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.listagem.BuscarItensMovimentacaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.listagem.BuscarItensMovimentacaoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.listagem.BuscarItensMovimentacaoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.listagem.BuscarItensMovimentacaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.listagem.converter.BuscarItensMovimentacaoFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.listagem.converter.BuscarItensMovimentacaoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.listagem.exception.MovimentacaoNaoEncontradaException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BuscarItensMovimentacaoUseCaseTest {

    @Mock
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Mock
    private MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    private BuscarItensMovimentacaoUseCase useCase;

    @Before
    public void inicializar() {
        useCase = new BuscarItensMovimentacaoUseCaseImpl(
            movimentacaoDataProvider,
            movimentacaoItemDataProvider,
            new BuscarItensMovimentacaoFiltroConverter(),
            new BuscarItensMovimentacaoOutputDataConverter()
        );
    }

    @Test(expected = IllegalStateException.class)
    public void deveLancarExcecaoQuandoMovimentacaoIdForNulo() {
        useCase.executar(BuscarItensMovimentacaoInputData.builder().build());
    }

    @Test(expected = IllegalStateException.class)
    public void deveLancarExcecaoQuandoNumeroDaPaginaForNulo() {
        useCase.executar(BuscarItensMovimentacaoInputData.builder()
            .movimentacaoId(1L)
            .build());
    }

    @Test(expected = IllegalStateException.class)
    public void deveLancarExcecaoQuandoNumeroDaPaginaMenorQueZero() {
        useCase.executar(BuscarItensMovimentacaoInputData.builder()
            .movimentacaoId(1L)
            .page(-1L)
            .build());
    }

    @Test(expected = IllegalStateException.class)
    public void deveLancarExcecaoQuandoTamanhoDaPaginaForNulo() {
        useCase.executar(BuscarItensMovimentacaoInputData.builder()
            .movimentacaoId(1L)
            .page(1L)
            .build());
    }

    @Test(expected = IllegalStateException.class)
    public void deveLancarExcecaoQuandoTamanhoDaPaginaForMenorOuIgualZero() {
        useCase.executar(BuscarItensMovimentacaoInputData.builder()
            .movimentacaoId(1L)
            .page(1L)
            .size(0L)
            .build());
    }

    @Test(expected = MovimentacaoNaoEncontradaException.class)
    public void deveLancarExcecaoQuandoMovimentacaoNaoForEncontradaPeloIdInformado() {
        when(movimentacaoDataProvider.existe(anyLong())).thenReturn(false);

        useCase.executar(BuscarItensMovimentacaoInputData.builder()
            .movimentacaoId(1L)
            .page(1L)
            .size(10L)
            .build());
    }

    @Test
    public void deveBuscarOsItensDaMovimentacaoComOFiltroCorreto() {
        final BuscarItensMovimentacaoInputData inputData = BuscarItensMovimentacaoInputData.builder()
            .movimentacaoId(1L)
            .page(1L)
            .size(10L)
            .build();

        final MovimentacaoItem.Filtro filtroEsperado = new MovimentacaoItem.Filtro();
        filtroEsperado.setMovimentacaoId(inputData.getMovimentacaoId());
        filtroEsperado.setPage(inputData.getPage()-1);
        filtroEsperado.setSize(inputData.getSize());

        final Movimentacao movimentacao = Movimentacao.builder().id(1L).build();

        final ItemIncorporacao itemIncorporacaoFordKa = ItemIncorporacao.builder()
            .descricao("Ford Ka 1.0")
            .incorporacao(Incorporacao.builder().codigo("00001").build())
            .build();

        final ItemIncorporacao itemIncorporacaoFordKaSedan = ItemIncorporacao.builder()
            .descricao("Ford Ka Sedan 1.6")
            .incorporacao(Incorporacao.builder().codigo("00002").build())
            .build();

        final ItemIncorporacao itemIncorporacaoVWGol = ItemIncorporacao.builder()
            .descricao("Volkswagen Gol 1.0")
            .incorporacao(Incorporacao.builder().codigo("00003").build())
            .build();

        final ListaPaginada<MovimentacaoItem> movimentacaoItens = ListaPaginada.<MovimentacaoItem>builder()
            .items(Arrays.asList(
                MovimentacaoItem.builder()
                    .movimentacao(movimentacao)
                    .patrimonio(Patrimonio.builder()
                        .id(1L)
                        .numero(itemIncorporacaoFordKa.getCodigo())
                        .itemIncorporacao(itemIncorporacaoFordKa)
                        .build())
                    .build(),
                MovimentacaoItem.builder()
                    .movimentacao(movimentacao)
                    .patrimonio(Patrimonio.builder()
                        .id(2L)
                        .numero(itemIncorporacaoFordKaSedan.getCodigo())
                        .itemIncorporacao(itemIncorporacaoFordKaSedan)
                        .build())
                    .build(),
                MovimentacaoItem.builder()
                    .movimentacao(movimentacao)
                    .patrimonio(Patrimonio.builder()
                        .id(3L)
                        .numero(itemIncorporacaoVWGol.getCodigo())
                        .itemIncorporacao(itemIncorporacaoVWGol)
                        .build())
                    .build()
            ))
            .totalPages(1L)
            .totalElements(3L)
            .build();

        final BuscarItensMovimentacaoOutputData outputDataEsperado = BuscarItensMovimentacaoOutputData.builder()
            .items(Arrays.asList(
                BuscarItensMovimentacaoOutputData.Item.builder()
                    .patrimonioId(movimentacaoItens.getItems().get(0).getPatrimonio().getId())
                    .patrimonioNumero(movimentacaoItens.getItems().get(0).getPatrimonio().getNumero())
                    .patrimonioDescricao(movimentacaoItens.getItems().get(0).getPatrimonio().getItemIncorporacao().getDescricao())
                    .incorporacaoCodigo(movimentacaoItens.getItems().get(0).getPatrimonio().getItemIncorporacao().getIncorporacao().getCodigo())
                    .movimentacaoId(movimentacaoItens.getItems().get(0).getMovimentacao().getId())
                    .build(),
                BuscarItensMovimentacaoOutputData.Item.builder()
                    .patrimonioId(movimentacaoItens.getItems().get(1).getPatrimonio().getId())
                    .patrimonioNumero(movimentacaoItens.getItems().get(1).getPatrimonio().getNumero())
                    .patrimonioDescricao(movimentacaoItens.getItems().get(1).getPatrimonio().getItemIncorporacao().getDescricao())
                    .incorporacaoCodigo(movimentacaoItens.getItems().get(1).getPatrimonio().getItemIncorporacao().getIncorporacao().getCodigo())
                    .movimentacaoId(movimentacaoItens.getItems().get(1).getMovimentacao().getId())
                    .build(),
                BuscarItensMovimentacaoOutputData.Item.builder()
                    .patrimonioId(movimentacaoItens.getItems().get(2).getPatrimonio().getId())
                    .patrimonioNumero(movimentacaoItens.getItems().get(2).getPatrimonio().getNumero())
                    .patrimonioDescricao(movimentacaoItens.getItems().get(2).getPatrimonio().getItemIncorporacao().getDescricao())
                    .incorporacaoCodigo(movimentacaoItens.getItems().get(2).getPatrimonio().getItemIncorporacao().getIncorporacao().getCodigo())
                    .movimentacaoId(movimentacaoItens.getItems().get(2).getMovimentacao().getId())
                    .build()
            ))
            .totalPages(movimentacaoItens.getTotalPages())
            .totalElements(movimentacaoItens.getTotalElements())
            .build();

        when(movimentacaoDataProvider.existe(anyLong())).thenReturn(true);

        when(movimentacaoItemDataProvider.buscarPorMovimentacaoId(any(MovimentacaoItem.Filtro.class)))
            .thenReturn(movimentacaoItens);

        final BuscarItensMovimentacaoOutputData outputData = useCase.executar(inputData);

        assertEquals(outputDataEsperado, outputData);
        verify(movimentacaoItemDataProvider, times(1)).buscarPorMovimentacaoId(filtroEsperado);
    }
}
