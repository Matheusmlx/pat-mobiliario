package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria;

import br.com.azi.patrimoniomobiliario.domain.entity.ContaContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.MovimentacaoItem;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.LancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.devolucao.DevolverMovimentacaoTemporariaInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.devolucao.DevolverMovimentacaoTemporariaUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.devolucao.DevolverMovimentacaoTemporariaUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.devolucao.converter.DevolverMovimentacaoTemporariaOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.devolucao.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.devolucao.exception.MovimentacaoNaoEstaAguardandoDevolucaoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.devolucao.exception.NenhumPatrimonioSelecionadoParaDevolucaoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.devolucao.exception.PatrimonioJaDevolvidoException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DevolverMovimentacaoTemporariaUseCaseTest {

    private final LocalDate LOCAL_DATE = LocalDate.of(2021, 1, 15);

    @Mock
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Mock
    private MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    @Mock
    private PatrimonioDataProvider patrimonioDataProvider;

    @Mock
    private LancamentoContabilDataProvider lancamentoContabilDataProvider;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    private final Clock fixedClock = Clock.fixed(LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

    private DevolverMovimentacaoTemporariaUseCase useCase;

    private final LocalDateTime dataFinalizacao = LocalDateTime.of(2021, 1, 15, 0, 0);

    private final UnidadeOrganizacional setorDestino = UnidadeOrganizacional.builder().id(1L).build();

    private final UnidadeOrganizacional setorOrigem = UnidadeOrganizacional.builder().id(2L).build();

    private Movimentacao movimentacao;

    private Movimentacao movimentacaoDevolvida;

    private Movimentacao movimentacaoDevolvidaParcial;

    private Patrimonio patrimonioEmprestado;

    private Patrimonio patrimonioDevolvido;

    private MovimentacaoItem movimentacaoItem;

    private MovimentacaoItem movimentacaoItemDevolvido;

    @Before
    public void inicializar() {
        useCase = new DevolverMovimentacaoTemporariaUseCaseImpl(
            movimentacaoDataProvider,
            movimentacaoItemDataProvider,
            patrimonioDataProvider,
            lancamentoContabilDataProvider,
            fixedClock,
            new DevolverMovimentacaoTemporariaOutputDataConverter()
        );

        movimentacao = Movimentacao.builder()
            .id(12L)
            .situacao(Movimentacao.Situacao.AGUARDANDO_DEVOLUCAO)
            .setorOrigem(setorOrigem)
            .setorDestino(setorDestino)
            .build();

        movimentacaoDevolvida = Movimentacao.builder()
            .id(12L)
            .situacao(Movimentacao.Situacao.DEVOLVIDO)
            .dataFinalizacao(dataFinalizacao)
            .setorOrigem(setorOrigem)
            .setorDestino(setorDestino)
            .build();

        movimentacaoDevolvidaParcial = Movimentacao.builder()
            .id(12L)
            .situacao(Movimentacao.Situacao.DEVOLVIDO_PARCIAL)
            .dataFinalizacao(dataFinalizacao)
            .setorOrigem(setorOrigem)
            .setorDestino(setorDestino)
            .build();

        patrimonioEmprestado = Patrimonio.builder()
            .id(1L)
            .contaContabilClassificacao(ContaContabil.builder().id(1L).build())
            .setor(setorDestino)
            .build();

        patrimonioDevolvido = Patrimonio.builder()
            .id(1L)
            .contaContabilClassificacao(ContaContabil.builder().id(1L).build())
            .setor(setorOrigem)
            .build();

        movimentacaoItem = MovimentacaoItem.builder()
            .movimentacao(movimentacao)
            .patrimonio(patrimonioEmprestado)
            .build();

        movimentacaoItemDevolvido = MovimentacaoItem.builder()
            .movimentacao(movimentacao)
            .patrimonio(patrimonioDevolvido)
            .dataDevolucao(dataFinalizacao)
            .build();

    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoIdMovimentacaoNulo() {
        DevolverMovimentacaoTemporariaInputData inputData = new DevolverMovimentacaoTemporariaInputData();

        useCase.executar(inputData);
    }

    @Test(expected = MovimentacaoNaoEncontradaException.class)
    public void deveFalharQuandoMovimentacaoNaoEncontrada() {
        DevolverMovimentacaoTemporariaInputData inputData = DevolverMovimentacaoTemporariaInputData.builder()
            .movimentacaoId(1L)
            .patrimoniosId(Collections.singletonList(1L))
            .build();

        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.empty());

        useCase.executar(inputData);
    }

    @Test(expected = NenhumPatrimonioSelecionadoParaDevolucaoException.class)
    public void deveLancarExcecaoQuandoListaPatrimonioForVazia() {
        useCase.executar(DevolverMovimentacaoTemporariaInputData.builder()
            .movimentacaoId(1L)
            .patrimoniosId(new ArrayList<>())
            .build());
    }

    @Test(expected = MovimentacaoNaoEstaAguardandoDevolucaoException.class)
    public void deveFalharSeMovimentacaoNaoEstaAguardandoDevolucao() {
        DevolverMovimentacaoTemporariaInputData inputData = DevolverMovimentacaoTemporariaInputData.builder()
            .movimentacaoId(1L)
            .patrimoniosId(Collections.singletonList(1L))
            .build();

        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(
                Movimentacao
                    .builder()
                    .id(12L)
                    .situacao(Movimentacao.Situacao.DEVOLVIDO)
                    .build()
            ));

        useCase.executar(inputData);
    }

    @Test(expected = PatrimonioJaDevolvidoException.class)
    public void deveFalharSePatrimonioJaFoiDevolvido() {
        DevolverMovimentacaoTemporariaInputData inputData = DevolverMovimentacaoTemporariaInputData.builder()
            .movimentacaoId(1L)
            .patrimoniosId(Collections.singletonList(1L))
            .build();

        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(movimentacao));


        Mockito.when(patrimonioDataProvider.buscarTodosPatrimonios(anyList()))
            .thenReturn(List.of(patrimonioDevolvido));

        Mockito.when(movimentacaoItemDataProvider.aguardandoDevolucao(anyLong(), anyLong()))
            .thenReturn(false);

        useCase.executar(inputData);
    }

    @Test
    public void deveDevolverTodosPatrimoniosDaMovimentacao() {
        DevolverMovimentacaoTemporariaInputData inputData = DevolverMovimentacaoTemporariaInputData.builder()
            .movimentacaoId(1L)
            .devolverTodos(true)
            .build();

        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(movimentacao));

        Mockito.when(movimentacaoItemDataProvider.buscarItensAguardandoDevolucao(anyLong()))
            .thenReturn(List.of(movimentacaoItem));

        Mockito.when(movimentacaoDataProvider.salvar(any(Movimentacao.class)))
            .thenReturn(movimentacaoDevolvida);

        Mockito.when(movimentacaoItemDataProvider.buscarPorMovimentacaoEPatrimonio(anyLong(), anyLong()))
            .thenReturn(Optional.of(movimentacaoItem));

        useCase.executar(inputData);

        verify(patrimonioDataProvider, times(1))
            .atualizarTodos(List.of(patrimonioDevolvido));

        verify(movimentacaoDataProvider, times(1))
            .salvar(movimentacaoDevolvida);

        verify(movimentacaoItemDataProvider, times(1))
            .salvar(movimentacaoItemDevolvido);
    }

    @Test
    public void deveDevolverPatrimoniosParcialDaMovimentacao() {
        DevolverMovimentacaoTemporariaInputData inputData = DevolverMovimentacaoTemporariaInputData.builder()
            .movimentacaoId(1L)
            .patrimoniosId(Collections.singletonList(1L))
            .build();

        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(movimentacao));

        Mockito.when(movimentacaoItemDataProvider.buscarPorMovimentacaoEPatrimonio(anyLong(), anyLong()))
            .thenReturn(Optional.of(movimentacaoItem));

        Mockito.when(movimentacaoItemDataProvider.existeItemAguardandoDevolucaoPorMovimentacao(anyLong()))
            .thenReturn(Boolean.TRUE);

        Mockito.when(patrimonioDataProvider.buscarTodosPatrimonios(anyList()))
            .thenReturn(List.of(patrimonioEmprestado));

        Mockito.when(movimentacaoDataProvider.salvar(any(Movimentacao.class)))
            .thenReturn(movimentacaoDevolvidaParcial);

        Mockito.when(movimentacaoItemDataProvider.aguardandoDevolucao(anyLong(), anyLong()))
            .thenReturn(true);

        useCase.executar(inputData);

        verify(patrimonioDataProvider, times(1))
            .atualizarTodos(List.of(patrimonioDevolvido));

        verify(movimentacaoDataProvider, times(1))
            .salvar(movimentacaoDevolvidaParcial);

        verify(movimentacaoItemDataProvider, times(1))
            .salvar(movimentacaoItemDevolvido);

    }
}
