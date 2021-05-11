package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria;

import br.com.azi.patrimoniomobiliario.domain.constant.movimentacao.TipoMovimentacaoEnum;
import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.ItemIncorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.MovimentacaoItem;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.bucarpatrimoniosdevolucao.BuscarPatrimoniosDevolucaoMovimentacaoTemporariaInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.bucarpatrimoniosdevolucao.BuscarPatrimoniosDevolucaoMovimentacaoTemporariaOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.bucarpatrimoniosdevolucao.BuscarPatrimoniosDevolucaoMovimentacaoTemporariaUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.bucarpatrimoniosdevolucao.BuscarPatrimoniosDevolucaoMovimentacaoTemporariaUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.bucarpatrimoniosdevolucao.converter.BuscarPatrimoniosDevolucaoMovimentacaoTemporariaFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.bucarpatrimoniosdevolucao.converter.BuscarPatrimoniosDevolucaoMovimentacaoTemporariaOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.bucarpatrimoniosdevolucao.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.bucarpatrimoniosdevolucao.exception.MovimentacaoNaoEstaAguardandoDevolucaoException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BuscarPatrimoniosDevolucaoMovimentacaoTemporariaUseCaseTest {

    @Mock
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Mock
    private PatrimonioDataProvider patrimonioDataProvider;

    @InjectMocks
    private BuscarPatrimoniosDevolucaoMovimentacaoTemporariaFiltroConverter filtroConverter;

    @InjectMocks
    private BuscarPatrimoniosDevolucaoMovimentacaoTemporariaOutputDataConverter converter;

    private BuscarPatrimoniosDevolucaoMovimentacaoTemporariaUseCase useCase;

    @Before
    public void inicializar() {
        useCase = new BuscarPatrimoniosDevolucaoMovimentacaoTemporariaUseCaseImpl(
            movimentacaoDataProvider,
            patrimonioDataProvider,
            filtroConverter,
            converter);
    }

    @Test(expected = IllegalStateException.class)
    public void deveLancarExcecaoQuandoMovimentacaoIdForNulo() {
        useCase.executar(new BuscarPatrimoniosDevolucaoMovimentacaoTemporariaInputData());
    }

    @Test(expected = IllegalStateException.class)
    public void deveLancarExcecaoQuandoNumeroDaPaginaForNulo() {
        useCase.executar(BuscarPatrimoniosDevolucaoMovimentacaoTemporariaInputData.builder()
            .movimentacaoId(1L)
            .build());
    }

    @Test(expected = IllegalStateException.class)
    public void deveLancarExcecaoQuandoTamanhoDaPaginaForNulo() {
        useCase.executar(BuscarPatrimoniosDevolucaoMovimentacaoTemporariaInputData.builder()
            .movimentacaoId(1L)
            .page(1L)
            .build());
    }

    @Test(expected = IllegalStateException.class)
    public void deveLancarExcecaoQuandoTamanhoDaPaginaForMenorOuIgualZero() {
        useCase.executar(BuscarPatrimoniosDevolucaoMovimentacaoTemporariaInputData.builder()
            .movimentacaoId(1L)
            .page(1L)
            .size(0L)
            .build());
    }

    @Test(expected = MovimentacaoNaoEncontradaException.class)
    public void deveLancarExcecaoQuandoMovimentacaoNaoForEncontradaPeloIdInformado() {
        useCase.executar(BuscarPatrimoniosDevolucaoMovimentacaoTemporariaInputData.builder()
            .movimentacaoId(1L)
            .page(1L)
            .size(10L)
            .build());
    }



    @Test(expected = MovimentacaoNaoEstaAguardandoDevolucaoException.class)
    public void deveLancarExcecaoQuandoMovimentacaoNaoEstiverAguardandoDevolucao() {
        BuscarPatrimoniosDevolucaoMovimentacaoTemporariaInputData inputData = BuscarPatrimoniosDevolucaoMovimentacaoTemporariaInputData.builder()
            .movimentacaoId(1L)
            .page(1L)
            .size(10L)
            .build();

        MovimentacaoItem.Filtro filtroEsperado = new MovimentacaoItem.Filtro();
        filtroEsperado.setMovimentacaoId(inputData.getMovimentacaoId());
        filtroEsperado.setPage(inputData.getPage()-1);
        filtroEsperado.setSize(inputData.getSize());

        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(Movimentacao
                .builder()
                .id(1L)
                .tipo(TipoMovimentacaoEnum.TEMPORARIA)
                .situacao(Movimentacao.Situacao.DEVOLVIDO)
                .build()));

       useCase.executar(inputData);
    }

    @Test
    public void deveBuscarOsItensDaMovimentacaoComOFiltroCorreto() {
        BuscarPatrimoniosDevolucaoMovimentacaoTemporariaInputData inputData = BuscarPatrimoniosDevolucaoMovimentacaoTemporariaInputData.builder()
            .movimentacaoId(1L)
            .page(1L)
            .size(10L)
            .build();

        Patrimonio.Filtro filtroEsperado = new Patrimonio.Filtro();
        filtroEsperado.setPage(inputData.getPage()-1);
        filtroEsperado.setSize(inputData.getSize());

        ItemIncorporacao itemIncorporacaoFordKa = ItemIncorporacao.builder()
            .descricao("Ford Ka 1.0")
            .incorporacao(Incorporacao.builder().codigo("00001").build())
            .build();

        ItemIncorporacao itemIncorporacaoFordKaSedan = ItemIncorporacao.builder()
            .descricao("Ford Ka Sedan 1.6")
            .incorporacao(Incorporacao.builder().codigo("00002").build())
            .build();


        ListaPaginada<Patrimonio> patrimonios = ListaPaginada.<Patrimonio>builder()
            .items(Arrays.asList(
                Patrimonio.builder()
                        .id(1L)
                        .numero(itemIncorporacaoFordKa.getCodigo())
                        .itemIncorporacao(itemIncorporacaoFordKa)
                    .build(),
              Patrimonio.builder()
                        .id(2L)
                        .numero(itemIncorporacaoFordKaSedan.getCodigo())
                        .itemIncorporacao(itemIncorporacaoFordKaSedan)
                        .build()
            ))
            .totalPages(1L)
            .totalElements(3L)
            .build();

        BuscarPatrimoniosDevolucaoMovimentacaoTemporariaOutputData outputDataEsperado = BuscarPatrimoniosDevolucaoMovimentacaoTemporariaOutputData.builder()
            .itens(Arrays.asList(
                BuscarPatrimoniosDevolucaoMovimentacaoTemporariaOutputData.Item.builder()
                    .patrimonioId(patrimonios.getItems().get(0).getId())
                    .patrimonioNumero(patrimonios.getItems().get(0).getNumero())
                    .patrimonioDescricao(patrimonios.getItems().get(0).getItemIncorporacao().getDescricao())
                    .build(),
                BuscarPatrimoniosDevolucaoMovimentacaoTemporariaOutputData.Item.builder()
                    .patrimonioId(patrimonios.getItems().get(1).getId())
                    .patrimonioNumero(patrimonios.getItems().get(1).getNumero())
                    .patrimonioDescricao(patrimonios.getItems().get(1).getItemIncorporacao().getDescricao())
                    .build()
            ))
            .totalPages(patrimonios.getTotalPages())
            .totalElements(patrimonios.getTotalElements())
            .build();

        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(Movimentacao
            .builder()
                .id(1L)
                .tipo(TipoMovimentacaoEnum.TEMPORARIA)
                .situacao(Movimentacao.Situacao.AGUARDANDO_DEVOLUCAO)
            .build()));

        when(patrimonioDataProvider.buscarPatrimoniosAguardandoDevolucaoPorMovimentacao(any(Patrimonio.Filtro.class), anyLong()))
            .thenReturn(patrimonios);

        BuscarPatrimoniosDevolucaoMovimentacaoTemporariaOutputData outputData = useCase.executar(inputData);

        assertEquals(outputDataEsperado, outputData);
        verify(patrimonioDataProvider, times(1)).buscarPatrimoniosAguardandoDevolucaoPorMovimentacao(filtroEsperado, 1L);
    }
}
