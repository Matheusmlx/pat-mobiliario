package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques;

import br.com.azi.patrimoniomobiliario.domain.constant.movimentacao.TipoMovimentacaoEnum;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.NotaLancamentoContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotaLancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.edicao.EditarMovimentacaoEntreEstoquesInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.edicao.EditarMovimentacaoEntreEstoquesUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.edicao.EditarMovimentacaoEntreEstoquesUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.edicao.converter.EditarMovimentacaoEntreEstoquesOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.edicao.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.edicao.exception.MovimentacaoNaoEstaEmElaboracaoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.edicao.exception.SetorOrigemEDestinoSaoIguaisException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.edicao.exception.TipoDeMovimentacaoException;
import br.com.azi.patrimoniomobiliario.utils.date.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EditarMovimentacaoEntreEstoquesUseCaseTest {

    @Mock
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Mock
    private MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    @Mock
    private NotaLancamentoContabilDataProvider notaLancamentoContabilDataProvider;

    @InjectMocks
    private EditarMovimentacaoEntreEstoquesOutputDataConverter outputDataConverter;

    @Captor
    private ArgumentCaptor<Movimentacao> argumentCaptorMovimentacao;

    private EditarMovimentacaoEntreEstoquesUseCase useCase;

    private static final  LocalDateTime dataNotaContabil = LocalDateTime.of(2020, Month.JULY, 18, 10, 10, 10);
    private static final  Movimentacao movimentacaoSalvaDefault = Movimentacao.builder()
        .id(1L)
        .situacao(Movimentacao.Situacao.EM_ELABORACAO)
        .build();

    private Movimentacao movimentacao = new Movimentacao();

    @Before
    public void setupTest() {
        useCase = new EditarMovimentacaoEntreEstoquesUseCaseImpl(
            movimentacaoDataProvider,
            movimentacaoItemDataProvider,
            notaLancamentoContabilDataProvider,
            outputDataConverter
        );

        movimentacao = Movimentacao.builder()
            .id(1L)
            .tipo(TipoMovimentacaoEnum.ENTRE_ESTOQUES)
            .situacao(Movimentacao.Situacao.EM_ELABORACAO)
            .build();
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoIdMovimentacaoForNulo() {
        useCase.executar(new EditarMovimentacaoEntreEstoquesInputData());
    }

    @Test(expected = MovimentacaoNaoEncontradaException.class)
    public void deveFalharQuandoNaoEncontrarMovimentacaoPorId() {

        EditarMovimentacaoEntreEstoquesInputData inputData = EditarMovimentacaoEntreEstoquesInputData
            .builder()
            .id(1L)
            .build();
        when(movimentacaoDataProvider.existe(anyLong()))
            .thenReturn(false);

        useCase.executar(inputData);

        verify(movimentacaoDataProvider, times(1)).existe(1L);
    }

    @Test(expected = TipoDeMovimentacaoException.class)
    public void deveFalharQuandoMovimentacaoNaoForDoTipoMovimentacaoEntreEstoques() {
         EditarMovimentacaoEntreEstoquesInputData inputData = EditarMovimentacaoEntreEstoquesInputData.builder()
            .id(1L)
            .build();

        when(movimentacaoDataProvider.existe(anyLong())).thenReturn(true);
        when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(Movimentacao.builder()
                .id(1L)
                .tipo(TipoMovimentacaoEnum.ENTRE_SETORES)
                .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                .build()));

        useCase.executar(inputData);
    }

    @Test(expected = MovimentacaoNaoEstaEmElaboracaoException.class)
    public void deveFalharQuandoMovimentacaoEntreEstoquesNaoEstiverEmElaboracao() {
         EditarMovimentacaoEntreEstoquesInputData inputData = EditarMovimentacaoEntreEstoquesInputData.builder()
            .id(1L)
            .build();

        when(movimentacaoDataProvider.existe(anyLong())).thenReturn(true);
        when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(Movimentacao.builder()
                .id(1L)
                .tipo(TipoMovimentacaoEnum.ENTRE_ESTOQUES)
                .situacao(Movimentacao.Situacao.FINALIZADO)
                .build()));

        useCase.executar(inputData);
    }

    @Test(expected = SetorOrigemEDestinoSaoIguaisException.class)
    public void deveFalharQuandoSetoresForemIguais() {
        EditarMovimentacaoEntreEstoquesInputData inputData = EditarMovimentacaoEntreEstoquesInputData.builder()
            .id(1L)
            .orgao(1L)
            .setorOrigem(2L)
            .setorDestino(2L)
            .build();

        Mockito.when(movimentacaoDataProvider.existe(anyLong())).thenReturn(true);

        Mockito.when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(Movimentacao.builder()
                .id(1L)
                .tipo(TipoMovimentacaoEnum.ENTRE_ESTOQUES)
                .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                .build()));

        useCase.executar(inputData);
    }

    @Test
    public void devePreencherOrgaoOrigemEOrgaoDestinoQuandoOrgaoOrigemForInformado() {
        EditarMovimentacaoEntreEstoquesInputData inputData = EditarMovimentacaoEntreEstoquesInputData.builder()
            .id(1L)
            .orgao(1L)
            .build();

        when(movimentacaoItemDataProvider.existePorMovimentacaoId(anyLong())).thenReturn(false);
        when(movimentacaoDataProvider.existe(anyLong())).thenReturn(true);
        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(movimentacao));
        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(movimentacaoSalvaDefault);

        useCase.executar(inputData);

        verify(movimentacaoDataProvider, times(1)).buscarPorId(1L);
        verify(movimentacaoDataProvider, times(1)).salvar(argumentCaptorMovimentacao.capture());
        assertNotNull(argumentCaptorMovimentacao.getValue().getId());
        assertNotNull(argumentCaptorMovimentacao.getValue().getOrgaoOrigem());
        assertEquals(argumentCaptorMovimentacao.getValue().getOrgaoOrigem().getId(), Long.valueOf(1));
        assertNotNull(argumentCaptorMovimentacao.getValue().getOrgaoDestino());
        assertEquals(argumentCaptorMovimentacao.getValue().getOrgaoDestino().getId(), Long.valueOf(1));
    }

    @Test
    public void devePreencherSetorOrigemQuandoForInformado() {
        EditarMovimentacaoEntreEstoquesInputData inputData = EditarMovimentacaoEntreEstoquesInputData.builder()
            .id(1L)
            .orgao(1L)
            .setorOrigem(1L)
            .build();

        when(movimentacaoItemDataProvider.existePorMovimentacaoId(anyLong())).thenReturn(false);
        when(movimentacaoDataProvider.existe(anyLong())).thenReturn(true);
        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(movimentacao));
        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(movimentacaoSalvaDefault);

        useCase.executar(inputData);

        verify(movimentacaoDataProvider, times(1)).buscarPorId(1L);
        verify(movimentacaoDataProvider, times(1)).salvar(argumentCaptorMovimentacao.capture());
        assertNotNull(argumentCaptorMovimentacao.getValue().getId());
        assertNotNull(argumentCaptorMovimentacao.getValue().getOrgaoOrigem());
        assertNotNull(argumentCaptorMovimentacao.getValue().getSetorOrigem());
        assertEquals(argumentCaptorMovimentacao.getValue().getOrgaoOrigem().getId(), Long.valueOf(1));
        assertEquals(argumentCaptorMovimentacao.getValue().getSetorOrigem().getId(), Long.valueOf(1));
    }

    @Test
    public void devePreencherSetorDestinoQuandoForInformado() {
        EditarMovimentacaoEntreEstoquesInputData inputData = EditarMovimentacaoEntreEstoquesInputData.builder()
            .id(1L)
            .orgao(1L)
            .setorDestino(2L)
            .build();

        when(movimentacaoItemDataProvider.existePorMovimentacaoId(anyLong())).thenReturn(false);
        when(movimentacaoDataProvider.existe(anyLong())).thenReturn(true);
        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(movimentacao));
        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(movimentacaoSalvaDefault);

        useCase.executar(inputData);

        verify(movimentacaoDataProvider, times(1)).buscarPorId(1L);
        verify(movimentacaoDataProvider, times(1)).salvar(argumentCaptorMovimentacao.capture());
        assertNotNull(argumentCaptorMovimentacao.getValue().getId());
        assertNotNull(argumentCaptorMovimentacao.getValue().getOrgaoOrigem());
        assertNotNull(argumentCaptorMovimentacao.getValue().getSetorDestino());
        assertEquals(argumentCaptorMovimentacao.getValue().getOrgaoOrigem().getId(), Long.valueOf(1));
        assertEquals(argumentCaptorMovimentacao.getValue().getSetorDestino().getId(), Long.valueOf(2));
    }

    @Test
    public void deveRemoverOrgaoDestinoSetorOrigemESetorDestinoQuandoRemoverOrgaoOrigem() {
        EditarMovimentacaoEntreEstoquesInputData inputData = EditarMovimentacaoEntreEstoquesInputData.builder()
            .id(1L)
            .setorDestino(1L)
            .setorDestino(2L)
            .build();

        when(movimentacaoDataProvider.existe(anyLong())).thenReturn(true);
        when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(Movimentacao.builder()
                .id(1L)
                .tipo(TipoMovimentacaoEnum.ENTRE_ESTOQUES)
                .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                .orgaoOrigem(UnidadeOrganizacional.builder().id(1L).build())
                .setorOrigem(UnidadeOrganizacional.builder().id(2L).build())
                .setorDestino(UnidadeOrganizacional.builder().id(3L).build())
                .build()));
        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(movimentacaoSalvaDefault);

        useCase.executar(inputData);

        verify(movimentacaoDataProvider, times(1)).buscarPorId(1L);
        verify(movimentacaoDataProvider, times(1)).salvar(argumentCaptorMovimentacao.capture());
        assertNotNull(argumentCaptorMovimentacao.getValue().getId());
        assertNull(argumentCaptorMovimentacao.getValue().getOrgaoOrigem());
        assertNull(argumentCaptorMovimentacao.getValue().getOrgaoDestino());
        assertNull(argumentCaptorMovimentacao.getValue().getSetorOrigem());
        assertNull(argumentCaptorMovimentacao.getValue().getSetorDestino());
    }

    @Test
    public void deveRemoverItensDaMovimentacaoEntreEstoquesQuandoTrocarOrgaoOrigem() {
        EditarMovimentacaoEntreEstoquesInputData inputData = EditarMovimentacaoEntreEstoquesInputData.builder()
            .id(1L)
            .orgao(2L)
            .build();

        when(movimentacaoDataProvider.existe(anyLong())).thenReturn(true);
        when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(Movimentacao.builder()
                .id(1L)
                .tipo(TipoMovimentacaoEnum.ENTRE_ESTOQUES)
                .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                .orgaoOrigem(UnidadeOrganizacional.builder().id(1L).build())
                .build()));
        when(movimentacaoItemDataProvider.existePorMovimentacaoId(anyLong())).thenReturn(true);
        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(movimentacaoSalvaDefault);

        useCase.executar(inputData);

        verify(movimentacaoItemDataProvider, times(1)).existePorMovimentacaoId(1L);
        verify(movimentacaoItemDataProvider, times(1)).removerPorMovimentacao(1L);
    }

    @Test
    public void deveRemoverItensDaMovimentacaoEntreEstoquesQuandoRemoverOrgaoOrigem() {
        EditarMovimentacaoEntreEstoquesInputData inputData = EditarMovimentacaoEntreEstoquesInputData.builder()
            .id(1L)
            .build();

        when(movimentacaoDataProvider.existe(anyLong())).thenReturn(true);
        when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(Movimentacao.builder()
                .id(1L)
                .tipo(TipoMovimentacaoEnum.ENTRE_ESTOQUES)
                .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                .orgaoOrigem(UnidadeOrganizacional.builder().id(1L).build())
                .build()));
        when(movimentacaoItemDataProvider.existePorMovimentacaoId(anyLong())).thenReturn(true);
        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(movimentacaoSalvaDefault);

        useCase.executar(inputData);

        verify(movimentacaoItemDataProvider, times(1)).existePorMovimentacaoId(1L);
        verify(movimentacaoItemDataProvider, times(1)).removerPorMovimentacao(1L);
    }

    @Test
    public void naoDeveRemoverItensDaMovimentacaoEntreEstoquesQuandoTrocarOrgaoOrigemENaoPossuirItensAdicionados() {
        EditarMovimentacaoEntreEstoquesInputData inputData = EditarMovimentacaoEntreEstoquesInputData.builder()
            .id(1L)
            .orgao(2L)
            .build();

        when(movimentacaoDataProvider.existe(anyLong())).thenReturn(true);
        when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(Movimentacao.builder()
                .id(1L)
                .tipo(TipoMovimentacaoEnum.ENTRE_ESTOQUES)
                .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                .orgaoOrigem(UnidadeOrganizacional.builder().id(1L).build())
                .build()));
        when(movimentacaoItemDataProvider.existePorMovimentacaoId(anyLong())).thenReturn(false);
        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(movimentacaoSalvaDefault);

        useCase.executar(inputData);

        verify(movimentacaoItemDataProvider, times(1)).existePorMovimentacaoId(1L);
        verify(movimentacaoItemDataProvider, times(0)).removerPorMovimentacao(1L);
    }

    @Test
    public void naoDeveTentarRemoverItensDaMovimentacaoEntreEstoquesQuandoRemoverOrgaoOrigemENaoPossuirItensAdicionados() {
        EditarMovimentacaoEntreEstoquesInputData inputData = EditarMovimentacaoEntreEstoquesInputData.builder()
            .id(1L)
            .build();

        when(movimentacaoDataProvider.existe(anyLong())).thenReturn(true);
        when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(Movimentacao.builder()
                .id(1L)
                .tipo(TipoMovimentacaoEnum.ENTRE_ESTOQUES)
                .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                .orgaoOrigem(UnidadeOrganizacional.builder().id(1L).build())
                .build()));
        when(movimentacaoItemDataProvider.existePorMovimentacaoId(anyLong())).thenReturn(false);
        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(movimentacaoSalvaDefault);

        useCase.executar(inputData);

        verify(movimentacaoItemDataProvider, times(1)).existePorMovimentacaoId(1L);
        verify(movimentacaoItemDataProvider, times(0)).removerPorMovimentacao(1L);
    }

    @Test
    public void deveRemoverItensDaMovimentacaoEntreEstoquesQuandoTrocarSetorOrigem() {
        EditarMovimentacaoEntreEstoquesInputData inputData = EditarMovimentacaoEntreEstoquesInputData.builder()
            .id(1L)
            .orgao(1L)
            .setorOrigem(3L)
            .build();

        when(movimentacaoDataProvider.existe(anyLong())).thenReturn(true);
        when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(Movimentacao.builder()
            .id(1L)
            .tipo(TipoMovimentacaoEnum.ENTRE_ESTOQUES)
            .situacao(Movimentacao.Situacao.EM_ELABORACAO)
            .orgaoOrigem(UnidadeOrganizacional.builder().id(1L).build())
            .setorOrigem(UnidadeOrganizacional.builder().id(2L).build())
            .build()));
        when(movimentacaoItemDataProvider.existePorMovimentacaoId(anyLong())).thenReturn(true);
        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(movimentacaoSalvaDefault);

        useCase.executar(inputData);

        verify(movimentacaoItemDataProvider, times(1)).existePorMovimentacaoId(1L);
        verify(movimentacaoItemDataProvider, times(1)).removerPorMovimentacao(1L);
    }

    @Test
    public void deveRemoverItensDaMovimentacaoEntreEstoquesQuandoRemoverSetorOrigem() {
        EditarMovimentacaoEntreEstoquesInputData inputData = EditarMovimentacaoEntreEstoquesInputData.builder()
            .id(1L)
            .orgao(1L)
            .build();

        Movimentacao movimentacaoBusca = Movimentacao.builder()
            .id(1L)
            .tipo(TipoMovimentacaoEnum.ENTRE_ESTOQUES)
            .situacao(Movimentacao.Situacao.EM_ELABORACAO)
            .orgaoOrigem(UnidadeOrganizacional.builder().id(1L).build())
            .setorOrigem(UnidadeOrganizacional.builder().id(2L).build())
            .build();

        when(movimentacaoDataProvider.existe(anyLong())).thenReturn(true);
        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(movimentacaoBusca));
        when(movimentacaoItemDataProvider.existePorMovimentacaoId(anyLong())).thenReturn(true);
        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(movimentacaoSalvaDefault);

        useCase.executar(inputData);

        verify(movimentacaoItemDataProvider, times(1)).existePorMovimentacaoId(1L);
        verify(movimentacaoItemDataProvider, times(1)).removerPorMovimentacao(1L);
    }

    @Test
    public void naoDeveRemoverItensDaMovimentacaoEntreEstoquesQuandoTrocarSetorOrigemENaoPossuirItensAdicionados() {
         Long orgaoOrigemId = 1L;
         Long setorOrigemId = 2L;
         Long novoSetorOrigemId = 3L;

         EditarMovimentacaoEntreEstoquesInputData inputData = EditarMovimentacaoEntreEstoquesInputData.builder()
            .id(1L)
            .orgao(orgaoOrigemId)
            .setorOrigem(novoSetorOrigemId)
            .build();

         Movimentacao movimentacaoBusca = Movimentacao.builder()
            .id(1L)
            .tipo(TipoMovimentacaoEnum.ENTRE_ESTOQUES)
            .situacao(Movimentacao.Situacao.EM_ELABORACAO)
            .orgaoOrigem(UnidadeOrganizacional.builder().id(orgaoOrigemId).build())
            .setorOrigem(UnidadeOrganizacional.builder().id(setorOrigemId).build())
            .build();

        when(movimentacaoDataProvider.existe(anyLong())).thenReturn(true);
        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(movimentacaoBusca));
        when(movimentacaoItemDataProvider.existePorMovimentacaoId(anyLong())).thenReturn(false);
        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(movimentacaoSalvaDefault);

        useCase.executar(inputData);

        verify(movimentacaoItemDataProvider, times(1)).existePorMovimentacaoId(1L);
        verify(movimentacaoItemDataProvider, times(0)).removerPorMovimentacao(1L);
    }

    @Test
    public void naoDeveRemoverItensDaMovimentacaoEntreEstoquesQuandoRemoverSetorOrigemENaoPossuirItensAdicionados() {
         Long orgaoOrigemId = 1L;
         Long setorOrigemId = 2L;
         Long novoSetorOrigemId = null;

         EditarMovimentacaoEntreEstoquesInputData inputData = EditarMovimentacaoEntreEstoquesInputData.builder()
            .id(1L)
            .orgao(orgaoOrigemId)
            .setorOrigem(novoSetorOrigemId)
            .build();

         Movimentacao movimentacaoBusca = Movimentacao.builder()
            .id(1L)
            .tipo(TipoMovimentacaoEnum.ENTRE_ESTOQUES)
            .situacao(Movimentacao.Situacao.EM_ELABORACAO)
            .orgaoOrigem(UnidadeOrganizacional.builder().id(orgaoOrigemId).build())
            .setorOrigem(UnidadeOrganizacional.builder().id(setorOrigemId).build())
            .build();

        when(movimentacaoDataProvider.existe(anyLong())).thenReturn(true);
        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(movimentacaoBusca));
        when(movimentacaoItemDataProvider.existePorMovimentacaoId(anyLong())).thenReturn(false);
        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(movimentacaoSalvaDefault);

        useCase.executar(inputData);

        verify(movimentacaoItemDataProvider, times(1)).existePorMovimentacaoId(1L);
        verify(movimentacaoItemDataProvider, times(0)).removerPorMovimentacao(1L);
    }

    @Test
    public void devePreencherMotivoQuandoForInformado() {
         String motivo = "Motivo";
         EditarMovimentacaoEntreEstoquesInputData inputData = EditarMovimentacaoEntreEstoquesInputData.builder()
            .id(1L)
            .motivoObservacao(motivo)
            .build();

        when(movimentacaoDataProvider.existe(anyLong())).thenReturn(true);
        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(movimentacao));
        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(movimentacaoSalvaDefault);

        useCase.executar(inputData);

        verify(movimentacaoDataProvider, times(1)).buscarPorId(1L);
        verify(movimentacaoDataProvider, times(1)).salvar(argumentCaptorMovimentacao.capture());
        assertNotNull(argumentCaptorMovimentacao.getValue().getId());
        assertNotNull(argumentCaptorMovimentacao.getValue().getMotivoObservacao());
        assertEquals(argumentCaptorMovimentacao.getValue().getMotivoObservacao(), motivo);
    }

    @Test
    public void devePreencherNumeroProcessoQuandoForInformado() {
        String numeroProcesso = "2020NP2";
        EditarMovimentacaoEntreEstoquesInputData inputData = EditarMovimentacaoEntreEstoquesInputData.builder()
            .id(1L)
            .numeroProcesso(numeroProcesso)
            .build();

        when(movimentacaoDataProvider.existe(anyLong())).thenReturn(true);
        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(movimentacao));
        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(movimentacaoSalvaDefault);

        useCase.executar(inputData);

        verify(movimentacaoDataProvider, times(1)).buscarPorId(1L);
        verify(movimentacaoDataProvider, times(1)).salvar(argumentCaptorMovimentacao.capture());
        assertNotNull(argumentCaptorMovimentacao.getValue().getId());
        assertNotNull(argumentCaptorMovimentacao.getValue().getNumeroProcesso());
        assertEquals(argumentCaptorMovimentacao.getValue().getNumeroProcesso(), numeroProcesso);
    }

    @Test
    public void deveCriarUmaNotaLancamentoContabilQuandoPreencherONumeroENaoTiverNumeroEDataDaNotaPreenchidos() {
        EditarMovimentacaoEntreEstoquesInputData inputData = EditarMovimentacaoEntreEstoquesInputData.builder()
            .id(1L)
            .numeroNotaLancamentoContabil("2020NL123456")
            .build();

        NotaLancamentoContabil notaLancamentoContabilParaSalvar = NotaLancamentoContabil.builder()
            .numero("2020NL123456")
            .build();

        NotaLancamentoContabil notaLancamentoContabilSalva = NotaLancamentoContabil.builder()
            .id(1L)
            .numero("2020NL123456")
            .build();

        when(movimentacaoDataProvider.existe(anyLong())).thenReturn(true);
        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(movimentacao));
        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(movimentacaoSalvaDefault);

        when(notaLancamentoContabilDataProvider.existePorNumero(anyString())).thenReturn(false);
        when(notaLancamentoContabilDataProvider.salvar(any(NotaLancamentoContabil.class)))
            .thenReturn(notaLancamentoContabilSalva);

        useCase.executar(inputData);

        verify(movimentacaoDataProvider, times(1)).buscarPorId(1L);
        verify(movimentacaoDataProvider, times(1)).salvar(argumentCaptorMovimentacao.capture());
        verify(notaLancamentoContabilDataProvider, times(1)).existePorNumero("2020NL123456");
        verify(notaLancamentoContabilDataProvider, times(1)).salvar(notaLancamentoContabilParaSalvar);
        assertEquals(argumentCaptorMovimentacao.getValue().getNotaLancamentoContabil(), notaLancamentoContabilSalva);
    }

    @Test
    public void deveCriarUmaNotaLancamentoContabilQuandoPreencherADataENaoTiverNumeroEDataDaNotaPreenchidos() {
        EditarMovimentacaoEntreEstoquesInputData inputData = EditarMovimentacaoEntreEstoquesInputData.builder()
            .id(1L)
            .dataNotaLancamentoContabil(DateUtils.asDate(dataNotaContabil))
            .build();

        NotaLancamentoContabil notaLancamentoContabilParaSalvar = NotaLancamentoContabil.builder()
            .dataLancamento(dataNotaContabil)
            .build();

        NotaLancamentoContabil notaLancamentoContabilSalva = NotaLancamentoContabil.builder()
            .id(1L)
            .dataLancamento(dataNotaContabil)
            .build();

        when(movimentacaoDataProvider.existe(anyLong())).thenReturn(true);
        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(movimentacao));
        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(movimentacaoSalvaDefault);

        when(notaLancamentoContabilDataProvider.salvar(any(NotaLancamentoContabil.class)))
            .thenReturn(notaLancamentoContabilSalva);

        useCase.executar(inputData);

        verify(movimentacaoDataProvider, times(1)).buscarPorId(1L);
        verify(movimentacaoDataProvider, times(1)).salvar(argumentCaptorMovimentacao.capture());
        verify(notaLancamentoContabilDataProvider, times(1)).salvar(notaLancamentoContabilParaSalvar);
        assertEquals(argumentCaptorMovimentacao.getValue().getNotaLancamentoContabil(), notaLancamentoContabilSalva);
    }

    @Test
    public void deveAtualizarANotaLancamentoContabilExistenteQuandoAtualizarNumeroOuData() {
        EditarMovimentacaoEntreEstoquesInputData inputData = EditarMovimentacaoEntreEstoquesInputData.builder()
            .id(1L)
            .numeroNotaLancamentoContabil("2020NL123445")
            .build();

        NotaLancamentoContabil notaLancamentoContabilParaSalvar = NotaLancamentoContabil.builder()
            .id(1L)
            .numero("2020NL123445")
            .build();

        NotaLancamentoContabil notaLancamentoContabilSalva = NotaLancamentoContabil.builder()
            .id(1L)
            .numero("2020NL123445")
            .build();

        when(movimentacaoDataProvider.existe(anyLong())).thenReturn(true);
        when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(
                Movimentacao.builder()
                    .id(1L)
                    .tipo(TipoMovimentacaoEnum.ENTRE_ESTOQUES)
                    .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                    .notaLancamentoContabil(NotaLancamentoContabil.builder()
                        .id(1L)
                        .numero("2020NL123456")
                        .build())
                    .build()));

        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(movimentacaoSalvaDefault);
        when(notaLancamentoContabilDataProvider.existePorNumero(anyString())).thenReturn(false);
        when(notaLancamentoContabilDataProvider.salvar(any(NotaLancamentoContabil.class)))
            .thenReturn(notaLancamentoContabilSalva);

        useCase.executar(inputData);

        verify(movimentacaoDataProvider, times(1)).buscarPorId(1L);
        verify(movimentacaoDataProvider, times(1)).salvar(argumentCaptorMovimentacao.capture());
        verify(notaLancamentoContabilDataProvider, times(1)).existePorNumero("2020NL123445");
        verify(notaLancamentoContabilDataProvider, times(1)).salvar(notaLancamentoContabilParaSalvar);
        assertEquals(argumentCaptorMovimentacao.getValue().getNotaLancamentoContabil(), notaLancamentoContabilSalva);
    }

    @Test
    public void deveRemoverANotaLancamentoContabilQuandoOsCamposNumeroEDataForemNulos() {
        EditarMovimentacaoEntreEstoquesInputData inputData = EditarMovimentacaoEntreEstoquesInputData.builder()
            .id(1L)
            .build();

        when(movimentacaoDataProvider.existe(anyLong())).thenReturn(true);
        when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(
                Movimentacao.builder()
                    .id(1L)
                    .tipo(TipoMovimentacaoEnum.ENTRE_ESTOQUES)
                    .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                    .notaLancamentoContabil(NotaLancamentoContabil.builder()
                        .id(1L)
                        .numero("2020NL123456")
                        .build())
                    .build()));
        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(movimentacaoSalvaDefault);

        useCase.executar(inputData);

        verify(notaLancamentoContabilDataProvider, times(1)).remover(1L);
        verify(movimentacaoDataProvider, times(1)).salvar(argumentCaptorMovimentacao.capture());
        assertNull(argumentCaptorMovimentacao.getValue().getNotaLancamentoContabil());
    }

    @Test
    public void naoDeveCriarNotaLancamentoContabilQuandoOsCamposForemNulos() {
         EditarMovimentacaoEntreEstoquesInputData inputData = EditarMovimentacaoEntreEstoquesInputData.builder()
            .id(1L)
            .build();

        when(movimentacaoDataProvider.existe(anyLong())).thenReturn(true);
        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(movimentacao));
        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(movimentacaoSalvaDefault);

        useCase.executar(inputData);

        verifyZeroInteractions(notaLancamentoContabilDataProvider);
    }


}
