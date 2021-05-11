package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item;

import br.com.azi.patrimoniomobiliario.domain.constant.movimentacao.TipoMovimentacaoEnum;
import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.ItemIncorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.buscarpatrimoniosparaselecao.BuscarPatrimoniosParaSelecaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.buscarpatrimoniosparaselecao.BuscarPatrimoniosParaSelecaoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.buscarpatrimoniosparaselecao.BuscarPatrimoniosParaSelecaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.buscarpatrimoniosparaselecao.converter.BuscarPatrimoniosParaSelecaoFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.buscarpatrimoniosparaselecao.converter.BuscarPatrimoniosParaSelecaoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.buscarpatrimoniosparaselecao.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.buscarpatrimoniosparaselecao.exception.OrgaoOrigemNaoSelecionadoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.buscarpatrimoniosparaselecao.exception.SetorOrigemNaoSelecionadoException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class BuscarPatrimoniosParaSelecaoUseCaseTest {

    @Mock
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Mock
    private PatrimonioDataProvider patrimonioDataProvider;

    private BuscarPatrimoniosParaSelecaoUseCase useCase;

    @Before
    public void inicializar() {
        useCase = new BuscarPatrimoniosParaSelecaoUseCaseImpl(
            movimentacaoDataProvider,
            patrimonioDataProvider,
            new BuscarPatrimoniosParaSelecaoFiltroConverter(),
            new BuscarPatrimoniosParaSelecaoOutputDataConverter()
        );
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoFiltroIncompleto() {
        BuscarPatrimoniosParaSelecaoInputData inputData = BuscarPatrimoniosParaSelecaoInputData
            .builder()
            .movimentacaoId(15L)
            .size(10L)
            .build();

        useCase.executar(inputData);
    }

    @Test(expected = MovimentacaoNaoEncontradaException.class)
    public void deveFalharQuandoMovimentacaoNaoEncontrada() {
        BuscarPatrimoniosParaSelecaoInputData inputData = BuscarPatrimoniosParaSelecaoInputData
            .builder()
            .movimentacaoId(15L)
            .page(2L)
            .size(10L)
            .build();

        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.empty());

        useCase.executar(inputData);
    }

    @Test(expected = OrgaoOrigemNaoSelecionadoException.class)
    public void deveFalharQuandoMovimentacaoNaoTemOrgaoSelecionado() {
        BuscarPatrimoniosParaSelecaoInputData inputData = BuscarPatrimoniosParaSelecaoInputData
            .builder()
            .movimentacaoId(15L)
            .page(2L)
            .size(10L)
            .build();

        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(Movimentacao
                .builder()
                .id(15L)
                .codigo("00015")
                .tipo(TipoMovimentacaoEnum.DISTRIBUICAO)
                .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                .build()));

        useCase.executar(inputData);
    }

    @Test(expected = SetorOrigemNaoSelecionadoException.class)
    public void deveFalharQuandoMovimentacaoNaoTemSetorOrigemSelecionado() {
        BuscarPatrimoniosParaSelecaoInputData inputData = BuscarPatrimoniosParaSelecaoInputData
            .builder()
            .movimentacaoId(15L)
            .page(2L)
            .size(10L)
            .build();

        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(Movimentacao
                .builder()
                .id(15L)
                .codigo("00015")
                .tipo(TipoMovimentacaoEnum.DISTRIBUICAO)
                .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                .orgaoOrigem(UnidadeOrganizacional
                    .builder()
                    .id(7L)
                    .build())
                .build()));

        useCase.executar(inputData);
    }

    @Test
    public void deveBuscarPatrimoniosPorOrgaoSetorOrigem() {
        BuscarPatrimoniosParaSelecaoInputData inputData = BuscarPatrimoniosParaSelecaoInputData
            .builder()
            .movimentacaoId(15L)
            .page(1L)
            .direction("ASC")
            .size(10L)
            .build();

        Patrimonio.Filtro filtro = Patrimonio.Filtro
            .builder()
            .orgao(7L)
            .setor(8L)
            .build();

        filtro.setPage(0L);
        filtro.setSize(10L);
        filtro.setDirection("ASC");

        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(Movimentacao
                .builder()
                .id(15L)
                .codigo("00015")
                .tipo(TipoMovimentacaoEnum.DISTRIBUICAO)
                .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                .orgaoOrigem(UnidadeOrganizacional
                    .builder()
                    .id(7L)
                    .build())
                .setorOrigem(UnidadeOrganizacional
                    .builder()
                    .id(8L)
                    .build())
                .build()));

        Mockito.when(patrimonioDataProvider.buscarPatrimoniosQueNaoEstaoEmOutraMovimentacaoEmElaboracao(any(Patrimonio.Filtro.class)))
            .thenReturn(ListaPaginada
                .<Patrimonio>builder()
                .items(
                    List.of(
                        Patrimonio
                            .builder()
                            .id(8L)
                            .itemIncorporacao(
                                ItemIncorporacao
                                    .builder()
                                    .descricao("Item incorporação")
                                    .incorporacao(Incorporacao.builder().codigo("5").build())
                                    .build())
                            .build()))
                .totalPages(1L)
                .totalElements(1L)
                .build());

        useCase.executar(inputData);

        verify(movimentacaoDataProvider, times(1)).buscarPorId(15L);

        verify(patrimonioDataProvider, times(1)).buscarPatrimoniosQueNaoEstaoEmOutraMovimentacaoEmElaboracao(filtro);
    }

}
