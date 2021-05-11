package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.cadastro;

import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.MovimentacaoItem;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.cadastro.converter.CadastrarItemMovimentacaoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.cadastro.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.cadastro.exception.MovimentacaoNaoEstaEmElaboracaoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.cadastro.exception.NenhumPatrimonioSelecionadoParaMovimentacaoException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CadastrarItemMovimentacaoUseCaseTest {

    @Mock
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Mock
    private MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    @Mock
    private PatrimonioDataProvider patrimonioDataProvider;

    private CadastrarItemMovimentacaoUseCase useCase;

    @Before
    public void inicializar() {
        useCase = new CadastrarItemMovimentacaoUseCaseImpl(
            movimentacaoDataProvider,
            movimentacaoItemDataProvider,
            patrimonioDataProvider,
            new CadastrarItemMovimentacaoOutputDataConverter()
        );
    }

    @Test(expected = IllegalStateException.class)
    public void deveLancarExcecaoQuandoMovimentacaoIdForNulo() {
        useCase.executar(new CadastrarItemMovimentacaoInputData());
    }

    @Test(expected = IllegalStateException.class)
    public void deveLancarExcecaoQuandoListaPatrimonioForNula() {
        useCase.executar(CadastrarItemMovimentacaoInputData.builder()
            .movimentacaoId(1L)
            .build());
    }

    @Test(expected = NenhumPatrimonioSelecionadoParaMovimentacaoException.class)
    public void deveLancarExcecaoQuandoListaPatrimonioForVazia() {
        useCase.executar(CadastrarItemMovimentacaoInputData.builder()
            .movimentacaoId(1L)
            .patrimoniosId(new ArrayList<>())
            .build());
    }

    @Test(expected = MovimentacaoNaoEncontradaException.class)
    public void deveLancarExcecaoQuandoNaoEncontrarMovimentacaoPeloIdInformado() {
        final Long movimentacaoId = 1L;

        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.empty());

        useCase.executar(CadastrarItemMovimentacaoInputData.builder()
            .movimentacaoId(1L)
            .patrimoniosId(Collections.singletonList(1L))
            .build());

        verify(movimentacaoDataProvider, times(1)).buscarPorId(movimentacaoId);
    }

    @Test(expected = MovimentacaoNaoEstaEmElaboracaoException.class)
    public void deveLancarExcecaoQuandoMovimentacaoNaoEstiverEmModoElaboracao() {
        final Long movimentacaoId = 1L;

        when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(
                Movimentacao.builder()
                    .id(movimentacaoId)
                    .situacao(Movimentacao.Situacao.FINALIZADO)
                    .build()
            ));

        useCase.executar(CadastrarItemMovimentacaoInputData.builder()
            .movimentacaoId(1L)
            .patrimoniosId(Collections.singletonList(1L))
            .build());
    }

    @Test
    public void deveCadastrarOsItensDaMovimentacaoQuandoOsPatrimoniosSelecionadosEstiveremAtivos() {
        final Long movimentacaoId = 1L;
        final List<Long> patrimoniosId = Collections.singletonList(1L);

        final Movimentacao movimentacao = Movimentacao.builder()
            .id(movimentacaoId)
            .situacao(Movimentacao.Situacao.EM_ELABORACAO)
            .orgaoOrigem(UnidadeOrganizacional.builder().id(1L).build())
            .setorOrigem(UnidadeOrganizacional.builder().id(2L).build())
            .build();

        final MovimentacaoItem movimentacaoItem = MovimentacaoItem.builder()
            .movimentacao(movimentacao)
            .patrimonio(Patrimonio.builder().id(patrimoniosId.get(0)).build())
            .build();

        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(
            Movimentacao.builder()
                .id(movimentacaoId)
                .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                .orgaoOrigem(UnidadeOrganizacional.builder().id(1L).build())
                .setorOrigem(UnidadeOrganizacional.builder().id(2L).build())
                .build()));

        when(patrimonioDataProvider.buscarPatrimoniosAtivosPorIdQueNaoEstaoEmOutraMovimentacao(anyList(), anyLong(), anyLong()))
            .thenReturn(Collections.singletonList(Patrimonio.builder().id(patrimoniosId.get(0)).build()));

        useCase.executar(CadastrarItemMovimentacaoInputData.builder()
            .movimentacaoId(1L)
            .patrimoniosId(patrimoniosId)
            .build());

        verify(patrimonioDataProvider, times(1)).buscarPatrimoniosAtivosPorIdQueNaoEstaoEmOutraMovimentacao(
            patrimoniosId, movimentacao.getOrgaoOrigem().getId(), movimentacao.getSetorOrigem().getId());
        verify(movimentacaoItemDataProvider, times(1)).salvar(Collections.singletonList(movimentacaoItem));
    }

    @Test
    public void deveCadastrarOsItensDaMovimentacaoQuandoOsPatrimoniosSelecionadosEstiveremAtivosEMovimentacaoComErroProcessamento() {
        final Long movimentacaoId = 1L;
        final List<Long> patrimoniosId = Collections.singletonList(1L);

        final Movimentacao movimentacao = Movimentacao.builder()
            .id(movimentacaoId)
            .situacao(Movimentacao.Situacao.ERRO_PROCESSAMENTO)
            .orgaoOrigem(UnidadeOrganizacional.builder().id(1L).build())
            .setorOrigem(UnidadeOrganizacional.builder().id(2L).build())
            .build();

        final MovimentacaoItem movimentacaoItem = MovimentacaoItem.builder()
            .movimentacao(movimentacao)
            .patrimonio(Patrimonio.builder().id(patrimoniosId.get(0)).build())
            .build();

        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(
            Movimentacao.builder()
                .id(movimentacaoId)
                .situacao(Movimentacao.Situacao.ERRO_PROCESSAMENTO)
                .orgaoOrigem(UnidadeOrganizacional.builder().id(1L).build())
                .setorOrigem(UnidadeOrganizacional.builder().id(2L).build())
                .build()));

        when(patrimonioDataProvider.buscarPatrimoniosAtivosPorIdQueNaoEstaoEmOutraMovimentacao(anyList(), anyLong(), anyLong()))
            .thenReturn(Collections.singletonList(Patrimonio.builder().id(patrimoniosId.get(0)).build()));

        useCase.executar(CadastrarItemMovimentacaoInputData.builder()
            .movimentacaoId(1L)
            .patrimoniosId(patrimoniosId)
            .build());

        verify(patrimonioDataProvider, times(1)).buscarPatrimoniosAtivosPorIdQueNaoEstaoEmOutraMovimentacao(
            patrimoniosId, movimentacao.getOrgaoOrigem().getId(), movimentacao.getSetorOrigem().getId());
        verify(movimentacaoItemDataProvider, times(1)).salvar(Collections.singletonList(movimentacaoItem));
    }

    @Test
    public void deveBuscarTodosPatrimoniosDoOrgaoESetorOrigemParaAdicionarNaMovimentacaoQuandoSelecionarTodosSelecionados() {
        final Long movimentacaoId = 1L;
        final List<Long> patrimoniosId = Arrays.asList(1L, 2L);
        final List<Long> patrimoniosNaoConsiderar = Collections.singletonList(3L);
        final boolean todosSelecionados = true;

        final Movimentacao movimentacao = Movimentacao.builder()
            .id(movimentacaoId)
            .situacao(Movimentacao.Situacao.EM_ELABORACAO)
            .orgaoOrigem(UnidadeOrganizacional.builder().id(1L).build())
            .setorOrigem(UnidadeOrganizacional.builder().id(2L).build())
            .build();

        final MovimentacaoItem movimentacaoItem1 = MovimentacaoItem.builder()
            .movimentacao(movimentacao)
            .patrimonio(Patrimonio.builder().id(patrimoniosId.get(0)).build())
            .build();

        final MovimentacaoItem movimentacaoItem2 = MovimentacaoItem.builder()
            .movimentacao(movimentacao)
            .patrimonio(Patrimonio.builder().id(patrimoniosId.get(1)).build())
            .build();

        when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(
                Movimentacao.builder()
                    .id(movimentacaoId)
                    .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                    .orgaoOrigem(UnidadeOrganizacional.builder().id(1L).build())
                    .setorOrigem(UnidadeOrganizacional.builder().id(2L).build())
                    .build()));

        when(patrimonioDataProvider.buscarPatrimoniosAtivosPorOrgaoSetorQueNaoEstaoEmOutraMovimentacao(anyList(), anyLong(), anyLong()))
            .thenReturn(Arrays.asList(
                Patrimonio.builder().id(patrimoniosId.get(0)).build(),
                Patrimonio.builder().id(patrimoniosId.get(1)).build()
            ));

        useCase.executar(CadastrarItemMovimentacaoInputData.builder()
            .movimentacaoId(1L)
            .todosSelecionados(todosSelecionados)
            .patrimoniosNaoConsiderar(patrimoniosNaoConsiderar)
            .build());

        verify(patrimonioDataProvider, times(1))
            .buscarPatrimoniosAtivosPorOrgaoSetorQueNaoEstaoEmOutraMovimentacao(
                patrimoniosNaoConsiderar,
                movimentacao.getOrgaoOrigem().getId(),
                movimentacao.getSetorOrigem().getId());

        verify(movimentacaoItemDataProvider, times(1)).salvar(
            Arrays.asList(movimentacaoItem1, movimentacaoItem2));
    }
}
