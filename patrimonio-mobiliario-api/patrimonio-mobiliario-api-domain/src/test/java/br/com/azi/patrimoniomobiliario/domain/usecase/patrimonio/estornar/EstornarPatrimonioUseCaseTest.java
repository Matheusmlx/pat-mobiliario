package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.estornar;

import br.com.azi.patrimoniomobiliario.domain.entity.ContaContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.ItemIncorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.LancamentoContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ItemIncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.LancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.estornar.exception.IncorporacaoComPatrimoniosEmOutrasMovimentacoesException;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.estornar.exception.IncorporacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.estornar.exception.IncorporacaoNaoPodeSerEstornadaException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EstornarPatrimonioUseCaseTest {

    @Mock
    private IncorporacaoDataProvider incorporacaoDataProvider;

    @Mock
    private ItemIncorporacaoDataProvider itemIncorporacaoDataProvider;

    @Mock
    private PatrimonioDataProvider patrimonioDataProvider;

    @Mock
    private LancamentoContabilDataProvider lancamentoContabilDataProvider;

    @Captor
    private ArgumentCaptor<LancamentoContabil> lancamentoContabilArgumentCaptor;

    @Captor
    private ArgumentCaptor<Incorporacao> incorporacaoArgumentCaptor;

    private EstornarPatrimonioUseCaseImpl useCase;

    @Before
    public void setUp() {
        useCase = new EstornarPatrimonioUseCaseImpl(
            incorporacaoDataProvider,
            itemIncorporacaoDataProvider,
            patrimonioDataProvider,
            lancamentoContabilDataProvider
        );
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoNaoInformadoPatrimoniosId() {
        EstornarPatrimonioInputData inputData = EstornarPatrimonioInputData
            .builder()
            .incorporacaoId(1L)
            .motivo("motivo")
            .usuario("admin")
            .data(new Date())
            .build();

        useCase.executar(inputData);
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoNaoInformadoIncorporacaoId() {
        EstornarPatrimonioInputData inputData = EstornarPatrimonioInputData
            .builder()
            .patrimoniosId(List.of(1L, 2L))
            .motivo("motivo")
            .usuario("admin")
            .data(new Date())
            .build();

        useCase.executar(inputData);
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoNaoInformadoMotivo() {
        EstornarPatrimonioInputData inputData = EstornarPatrimonioInputData
            .builder()
            .patrimoniosId(List.of(1L, 2L))
            .incorporacaoId(1L)
            .usuario("admin")
            .data(new Date())
            .build();

        useCase.executar(inputData);
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoNaoInformadoUsuario() {
        EstornarPatrimonioInputData inputData = EstornarPatrimonioInputData
            .builder()
            .patrimoniosId(List.of(1L, 2L))
            .incorporacaoId(1L)
            .motivo("motivo")
            .data(new Date())
            .build();


        useCase.executar(inputData);
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoNaoInformadoData() {
        EstornarPatrimonioInputData inputData = EstornarPatrimonioInputData
            .builder()
            .patrimoniosId(List.of(1L, 2L))
            .incorporacaoId(1L)
            .motivo("motivo")
            .usuario("admin")
            .build();

        useCase.executar(inputData);
    }

    @Test(expected = IncorporacaoNaoEncontradaException.class)
    public void deveFalharQuandoNaoEncontrarIncorporacao() {
        EstornarPatrimonioInputData inputData = EstornarPatrimonioInputData
            .builder()
            .patrimoniosId(List.of(1L, 2L))
            .incorporacaoId(1L)
            .motivo("motivo")
            .usuario("admin")
            .data(new Date())
            .build();

        when(incorporacaoDataProvider.buscarPorId(any(Long.class))).thenReturn(Optional.empty());

        useCase.executar(inputData);
    }

    @Test(expected = IncorporacaoComPatrimoniosEmOutrasMovimentacoesException.class)
    public void deveFalharQuandoIncorporacaoPossuirPatrimoniosQueJaEstaoEmOutrasMovimentacoes() {
        EstornarPatrimonioInputData inputData = EstornarPatrimonioInputData
            .builder()
            .patrimoniosId(List.of(1L, 2L))
            .incorporacaoId(1L)
            .motivo("motivo")
            .usuario("admin")
            .data(new Date())
            .build();

        when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Incorporacao.builder().situacao(Incorporacao.Situacao.FINALIZADO).id(1L).build()));

        when(patrimonioDataProvider.buscarQuantidadePatrimoniosEmOutrasMovimentacoesPorIncorporacaoId(any(Long.class)))
            .thenReturn(1L);

        useCase.executar(inputData);
    }

    @Test(expected = IncorporacaoNaoPodeSerEstornadaException.class)
    public void deveFalharQuandoIncorporacaoNaoEstiverFinalizadoOuParcialmenteEstornado() {
        EstornarPatrimonioInputData inputData = EstornarPatrimonioInputData
            .builder()
            .patrimoniosId(List.of(1L, 2L))
            .incorporacaoId(1L)
            .motivo("motivo")
            .usuario("admin")
            .data(new Date())
            .build();

        when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Incorporacao.builder().situacao(Incorporacao.Situacao.EM_PROCESSAMENTO).id(1L).build()));

        useCase.executar(inputData);
    }

    @Test
    public void deveEstornarPatrimonio() {
        EstornarPatrimonioInputData inputData = EstornarPatrimonioInputData
            .builder()
            .patrimoniosId(List.of(1L, 2L))
            .incorporacaoId(1L)
            .motivo("motivo")
            .usuario("admin")
            .data(new Date())
            .build();

        when(patrimonioDataProvider.buscarTodosPatrimonios(any(List.class))).thenReturn(
            List.of(
                Patrimonio
                    .builder()
                    .id(1L)
                    .valorAquisicao(BigDecimal.TEN)
                    .orgao(UnidadeOrganizacional.builder().id(1L).build())
                    .setor(UnidadeOrganizacional.builder().id(1L).build())
                    .contaContabilAtual(ContaContabil.builder().id(1L).build())
                    .itemIncorporacao(ItemIncorporacao
                        .builder()
                        .id(1L)
                        .build())
                    .build(),
                Patrimonio
                    .builder()
                    .id(2L)
                    .valorAquisicao(BigDecimal.TEN)
                    .orgao(UnidadeOrganizacional.builder().id(1L).build())
                    .setor(UnidadeOrganizacional.builder().id(1L).build())
                    .contaContabilAtual(ContaContabil.builder().id(1L).build())
                    .itemIncorporacao(ItemIncorporacao
                        .builder()
                        .id(2L)
                        .build())
                    .build()));

        when(itemIncorporacaoDataProvider.buscarItensPorIncorporacaoId(any(Long.class))).thenReturn(
            List.of(
                ItemIncorporacao
                    .builder()
                    .id(1L)
                    .valorTotal(BigDecimal.TEN)
                    .quantidade(1)
                    .build(),
                ItemIncorporacao
                    .builder()
                    .quantidade(1)
                    .valorTotal(BigDecimal.TEN)
                    .id(2L)
                    .build()));

        when(incorporacaoDataProvider.buscarPorId(any(Long.class))).thenReturn(Optional.of(Incorporacao.builder().situacao(Incorporacao.Situacao.FINALIZADO).id(1L).build()));

        useCase.executar(inputData);

        InOrder inOrder = Mockito.inOrder(itemIncorporacaoDataProvider,itemIncorporacaoDataProvider, patrimonioDataProvider, lancamentoContabilDataProvider, incorporacaoDataProvider);

        inOrder.verify(lancamentoContabilDataProvider, times(2)).salvar(lancamentoContabilArgumentCaptor.capture());

        inOrder.verify(incorporacaoDataProvider, times(1)).salvar(incorporacaoArgumentCaptor.capture());
    }

    @Test
    public void deveEstornarPatrimonioComTodosSelecionados() {
        EstornarPatrimonioInputData inputData = EstornarPatrimonioInputData
            .builder()
            .patrimoniosId(new ArrayList<Long>())
            .patrimoniosExcecao(new ArrayList<>())
            .incorporacaoId(1L)
            .motivo("motivo")
            .usuario("admin")
            .data(new Date())
            .todosSelecionados(true)
            .build();

        when(patrimonioDataProvider.buscarPatrimoniosAtivosPorIncorporacaoId(any(Long.class), anyList())).thenReturn(
            List.of(
                Patrimonio
                    .builder()
                    .id(1L)
                    .valorAquisicao(BigDecimal.TEN)
                    .orgao(UnidadeOrganizacional.builder().id(1L).build())
                    .setor(UnidadeOrganizacional.builder().id(1L).build())
                    .contaContabilAtual(ContaContabil.builder().id(1L).build())
                    .itemIncorporacao(ItemIncorporacao
                        .builder()
                        .id(1L)
                        .build())
                    .build(),
                Patrimonio
                    .builder()
                    .id(2L)
                    .valorAquisicao(BigDecimal.TEN)
                    .orgao(UnidadeOrganizacional.builder().id(1L).build())
                    .setor(UnidadeOrganizacional.builder().id(1L).build())
                    .contaContabilAtual(ContaContabil.builder().id(1L).build())
                    .itemIncorporacao(ItemIncorporacao
                        .builder()
                        .id(2L)
                        .build())
                    .build()));

        when(itemIncorporacaoDataProvider.buscarItensPorIncorporacaoId(any(Long.class))).thenReturn(
            List.of(
                ItemIncorporacao
                    .builder()
                    .id(1L)
                    .valorTotal(BigDecimal.TEN)
                    .quantidade(1)
                    .build(),
                ItemIncorporacao
                    .builder()
                    .quantidade(1)
                    .valorTotal(BigDecimal.TEN)
                    .id(2L)
                    .build()));

        when(incorporacaoDataProvider.buscarPorId(any(Long.class))).thenReturn(Optional.of(Incorporacao.builder().id(1L).situacao(Incorporacao.Situacao.FINALIZADO).build()));

        useCase.executar(inputData);

        InOrder inOrder = Mockito.inOrder(itemIncorporacaoDataProvider,itemIncorporacaoDataProvider, patrimonioDataProvider, lancamentoContabilDataProvider, incorporacaoDataProvider);

        inOrder.verify(patrimonioDataProvider, times(1)).buscarPatrimoniosAtivosPorIncorporacaoId(inputData.getIncorporacaoId(), inputData.getPatrimoniosExcecao());

        inOrder.verify(lancamentoContabilDataProvider, times(2)).salvar(lancamentoContabilArgumentCaptor.capture());

        inOrder.verify(incorporacaoDataProvider, times(1)).salvar(incorporacaoArgumentCaptor.capture());
    }

    @Test
    public void deveEstornarPatrimonioComQuantidadeIguais() {
        EstornarPatrimonioInputData inputData = EstornarPatrimonioInputData
            .builder()
            .patrimoniosId(List.of(1L, 2L))
            .incorporacaoId(1L)
            .motivo("motivo")
            .usuario("admin")
            .data(new Date())
            .todosSelecionados(false)
            .build();

        when(patrimonioDataProvider.buscarTodosPatrimonios(any(List.class))).thenReturn(
            List.of(
                Patrimonio
                    .builder()
                    .id(1L)
                    .valorAquisicao(BigDecimal.TEN)
                    .orgao(UnidadeOrganizacional.builder().id(1L).build())
                    .setor(UnidadeOrganizacional.builder().id(1L).build())
                    .contaContabilAtual(ContaContabil.builder().id(1L).build())
                    .itemIncorporacao(ItemIncorporacao
                        .builder()
                        .id(1L)
                        .build())
                    .build(),
                Patrimonio
                    .builder()
                    .id(2L)
                    .valorAquisicao(BigDecimal.TEN)
                    .orgao(UnidadeOrganizacional.builder().id(1L).build())
                    .setor(UnidadeOrganizacional.builder().id(1L).build())
                    .contaContabilAtual(ContaContabil.builder().id(1L).build())
                    .itemIncorporacao(ItemIncorporacao
                        .builder()
                        .id(2L)
                        .build())
                    .build()));

        when(itemIncorporacaoDataProvider.buscarItensPorIncorporacaoId(any(Long.class))).thenReturn(
            List.of(
                ItemIncorporacao
                    .builder()
                    .id(1L)
                    .valorTotal(BigDecimal.TEN)
                    .quantidade(1)
                    .build(),
                ItemIncorporacao
                    .builder()
                    .quantidade(1)
                    .valorTotal(BigDecimal.TEN)
                    .id(2L)
                    .build()));

        when(incorporacaoDataProvider.buscarPorId(any(Long.class))).thenReturn(Optional.of(Incorporacao.builder().id(1L).situacao(Incorporacao.Situacao.FINALIZADO).build()));

        when(patrimonioDataProvider.buscarQuantidadePatrimoniosAtivosPorIncorporacaoId(any(Long.class))).thenReturn(2L);

        useCase.executar(inputData);

        InOrder inOrder = Mockito.inOrder(itemIncorporacaoDataProvider,itemIncorporacaoDataProvider, patrimonioDataProvider, lancamentoContabilDataProvider, incorporacaoDataProvider);

        inOrder.verify(lancamentoContabilDataProvider, times(2)).salvar(lancamentoContabilArgumentCaptor.capture());

        inOrder.verify(incorporacaoDataProvider, times(1)).salvar(incorporacaoArgumentCaptor.capture());
    }
}
