package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.edicao;

import br.com.azi.patrimoniomobiliario.domain.constant.movimentacao.TipoMovimentacaoEnum;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.NotaLancamentoContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.Responsavel;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotaLancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.edicao.converter.EditarDistribuicaoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.edicao.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.edicao.exception.MovimentacaoNaoEstaEmElaboracaoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.edicao.exception.TipoDeMovimentacaoException;
import br.com.azi.patrimoniomobiliario.utils.date.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Date;
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
public class EditarDistribuicaoUseCaseTest {

    @Mock
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Mock
    private MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    @Mock
    private NotaLancamentoContabilDataProvider notaLancamentoContabilDataProvider;

    @Captor
    private ArgumentCaptor<Movimentacao> argumentCaptorMovimentacao;

    private EditarDistribuicaoUseCase useCase;

    private static final Long idMovimentacao = 1L;
    private static final LocalDateTime dataNotaContabil = LocalDateTime.of(2020, Month.JULY, 18, 10, 10, 10);
    private static final Movimentacao distribuicaoSalvaDefault = Movimentacao.builder()
        .id(idMovimentacao)
        .situacao(Movimentacao.Situacao.ERRO_PROCESSAMENTO)
        .build();

    @Before
    public void inicializar() {
        useCase = new EditarDistribuicaoUseCaseImpl(
            movimentacaoDataProvider,
            movimentacaoItemDataProvider,
            notaLancamentoContabilDataProvider,
            new EditarDistribuicaoOutputDataConverter()
        );
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoIdMovimentacaoForNulo() {
        final EditarDistribuicaoInputData inputData = EditarDistribuicaoInputData.builder().build();
        useCase.executar(inputData);
    }

    @Test(expected = MovimentacaoNaoEncontradaException.class)
    public void deveFalharQuandoNaoEncontrarMovimentacaoPorId() {
        final EditarDistribuicaoInputData inputData = EditarDistribuicaoInputData.builder()
            .id(idMovimentacao)
            .build();

        when(movimentacaoDataProvider.existe(anyLong())).thenReturn(false);

        useCase.executar(inputData);

        verify(movimentacaoDataProvider, times(1)).existe(idMovimentacao);
    }

    @Test(expected = TipoDeMovimentacaoException.class)
    public void deveFalharQuandoMovimentacaoNaoForDoTipoDistribuicao() {
        final EditarDistribuicaoInputData inputData = EditarDistribuicaoInputData.builder()
            .id(idMovimentacao)
            .build();

        when(movimentacaoDataProvider.existe(anyLong())).thenReturn(true);
        when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(Movimentacao.builder()
                .id(idMovimentacao)
                .tipo(TipoMovimentacaoEnum.ENTRE_ESTOQUES)
                .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                .build()));

        useCase.executar(inputData);
    }

    @Test(expected = MovimentacaoNaoEstaEmElaboracaoException.class)
    public void deveFalharQuandoDistribuicaoNaoEstiverEmModoElaboracao() {
        final EditarDistribuicaoInputData inputData = EditarDistribuicaoInputData.builder()
            .id(idMovimentacao)
            .build();

        when(movimentacaoDataProvider.existe(anyLong())).thenReturn(true);
        when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(Movimentacao.builder()
                .id(idMovimentacao)
                .tipo(TipoMovimentacaoEnum.DISTRIBUICAO)
                .situacao(Movimentacao.Situacao.FINALIZADO)
                .build()));

        useCase.executar(inputData);
    }

    @Test
    public void devePreencherOrgaoOrigemEOrgaoDestinoQuandoOrgaoOrigemForInformado() {
        final Long orgaoOrigemId = 1L;
        final EditarDistribuicaoInputData inputData = EditarDistribuicaoInputData.builder()
            .id(idMovimentacao)
            .orgao(orgaoOrigemId)
            .build();

        criarMocksTestePreenchimentoCampos();

        useCase.executar(inputData);

        verify(movimentacaoDataProvider, times(1)).buscarPorId(idMovimentacao);
        verify(movimentacaoDataProvider, times(1)).salvar(argumentCaptorMovimentacao.capture());
        assertNotNull(argumentCaptorMovimentacao.getValue().getId());
        assertNotNull(argumentCaptorMovimentacao.getValue().getOrgaoOrigem());
        assertEquals(argumentCaptorMovimentacao.getValue().getOrgaoOrigem().getId(), orgaoOrigemId);
        assertNotNull(argumentCaptorMovimentacao.getValue().getOrgaoDestino());
        assertEquals(argumentCaptorMovimentacao.getValue().getOrgaoDestino().getId(), orgaoOrigemId);
    }

    @Test
    public void devePreencherSetorOrigemQuandoForInformado() {
        final Long orgaoOrigemId = 1L;
        final Long setorOrigemId = 1L;
        final EditarDistribuicaoInputData inputData = EditarDistribuicaoInputData.builder()
            .id(idMovimentacao)
            .orgao(orgaoOrigemId)
            .setorOrigem(setorOrigemId)
            .build();

        criarMocksTestePreenchimentoCampos();

        useCase.executar(inputData);

        verify(movimentacaoDataProvider, times(1)).buscarPorId(idMovimentacao);
        verify(movimentacaoDataProvider, times(1)).salvar(argumentCaptorMovimentacao.capture());
        assertNotNull(argumentCaptorMovimentacao.getValue().getId());
        assertNotNull(argumentCaptorMovimentacao.getValue().getOrgaoOrigem());
        assertEquals(argumentCaptorMovimentacao.getValue().getOrgaoOrigem().getId(), orgaoOrigemId);
        assertNotNull(argumentCaptorMovimentacao.getValue().getSetorOrigem());
        assertEquals(argumentCaptorMovimentacao.getValue().getSetorOrigem().getId(), setorOrigemId);
    }

    @Test
    public void devePreencherSetorDestinoQuandoForInformado() {
        final Long orgaoOrigemId = 1L;
        final Long setorDestinoId = 2L;
        final EditarDistribuicaoInputData inputData = EditarDistribuicaoInputData.builder()
            .id(idMovimentacao)
            .orgao(orgaoOrigemId)
            .setorDestino(setorDestinoId)
            .build();

        criarMocksTestePreenchimentoCampos();

        useCase.executar(inputData);

        verify(movimentacaoDataProvider, times(1)).buscarPorId(idMovimentacao);
        verify(movimentacaoDataProvider, times(1)).salvar(argumentCaptorMovimentacao.capture());
        assertNotNull(argumentCaptorMovimentacao.getValue().getId());
        assertNotNull(argumentCaptorMovimentacao.getValue().getOrgaoOrigem());
        assertEquals(argumentCaptorMovimentacao.getValue().getOrgaoOrigem().getId(), orgaoOrigemId);
        assertNotNull(argumentCaptorMovimentacao.getValue().getSetorDestino());
        assertEquals(argumentCaptorMovimentacao.getValue().getSetorDestino().getId(), setorDestinoId);
    }

    @Test
    public void deveRemoverOrgaoDestinoSetorOrigemESetorDestinoQuandoRemoverOrgaoOrigem() {
        final Long orgaoOrigemId = null;
        final Long setorOrigemId = 1L;
        final Long setorDestinoId = 2L;
        final EditarDistribuicaoInputData inputData = EditarDistribuicaoInputData.builder()
            .id(idMovimentacao)
            .orgao(orgaoOrigemId)
            .setorDestino(setorOrigemId)
            .setorDestino(setorDestinoId)
            .build();

        when(movimentacaoDataProvider.existe(anyLong())).thenReturn(true);
        when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(Movimentacao.builder()
                .id(idMovimentacao)
                .tipo(TipoMovimentacaoEnum.DISTRIBUICAO)
                .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                .orgaoOrigem(UnidadeOrganizacional.builder().id(1L).build())
                .setorOrigem(UnidadeOrganizacional.builder().id(2L).build())
                .setorDestino(UnidadeOrganizacional.builder().id(3L).build())
                .build()));
        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(distribuicaoSalvaDefault);

        useCase.executar(inputData);

        verify(movimentacaoDataProvider, times(1)).buscarPorId(idMovimentacao);
        verify(movimentacaoDataProvider, times(1)).salvar(argumentCaptorMovimentacao.capture());
        assertNotNull(argumentCaptorMovimentacao.getValue().getId());
        assertNull(argumentCaptorMovimentacao.getValue().getOrgaoOrigem());
        assertNull(argumentCaptorMovimentacao.getValue().getOrgaoDestino());
        assertNull(argumentCaptorMovimentacao.getValue().getSetorOrigem());
        assertNull(argumentCaptorMovimentacao.getValue().getSetorDestino());
    }

    @Test
    public void deveRemoverItensDaDistribuicaoQuandoTrocarOrgaoOrigem() {
        final Long orgaoOrigemId = 1L;
        final Long novoOrgaoOrigemId = 2L;

        final EditarDistribuicaoInputData inputData = EditarDistribuicaoInputData.builder()
            .id(idMovimentacao)
            .orgao(novoOrgaoOrigemId)
            .build();

        when(movimentacaoDataProvider.existe(anyLong())).thenReturn(true);
        when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(Movimentacao.builder()
                .id(idMovimentacao)
                .tipo(TipoMovimentacaoEnum.DISTRIBUICAO)
                .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                .orgaoOrigem(UnidadeOrganizacional.builder().id(orgaoOrigemId).build())
                .build()));
        when(movimentacaoItemDataProvider.existePorMovimentacaoId(anyLong())).thenReturn(true);
        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(distribuicaoSalvaDefault);

        useCase.executar(inputData);

        verify(movimentacaoItemDataProvider, times(1)).existePorMovimentacaoId(idMovimentacao);
        verify(movimentacaoItemDataProvider, times(1)).removerPorMovimentacao(idMovimentacao);
    }

    @Test
    public void deveRemoverItensDaDistribuicaoQuandoRemoverOrgaoOrigem() {
        final Long orgaoOrigemId = 1L;
        final Long novoOrgaoOrigemId = null;

        final EditarDistribuicaoInputData inputData = EditarDistribuicaoInputData.builder()
            .id(idMovimentacao)
            .orgao(novoOrgaoOrigemId)
            .build();

        when(movimentacaoDataProvider.existe(anyLong())).thenReturn(true);
        when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(Movimentacao.builder()
                .id(idMovimentacao)
                .tipo(TipoMovimentacaoEnum.DISTRIBUICAO)
                .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                .orgaoOrigem(UnidadeOrganizacional.builder().id(orgaoOrigemId).build())
                .build()));
        when(movimentacaoItemDataProvider.existePorMovimentacaoId(anyLong())).thenReturn(true);
        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(distribuicaoSalvaDefault);

        useCase.executar(inputData);

        verify(movimentacaoItemDataProvider, times(1)).existePorMovimentacaoId(idMovimentacao);
        verify(movimentacaoItemDataProvider, times(1)).removerPorMovimentacao(idMovimentacao);
    }

    @Test
    public void naoDeveRemoverItensDaDistribuicaoQuandoTrocarOrgaoOrigemENaoPossuirItensAdicionados() {
        final Long orgaoOrigemId = 1L;
        final Long novoOrgaoOrigemId = 2L;

        final EditarDistribuicaoInputData inputData = EditarDistribuicaoInputData.builder()
            .id(idMovimentacao)
            .orgao(novoOrgaoOrigemId)
            .build();

        when(movimentacaoDataProvider.existe(anyLong())).thenReturn(true);
        when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(Movimentacao.builder()
                .id(idMovimentacao)
                .tipo(TipoMovimentacaoEnum.DISTRIBUICAO)
                .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                .orgaoOrigem(UnidadeOrganizacional.builder().id(orgaoOrigemId).build())
                .build()));
        when(movimentacaoItemDataProvider.existePorMovimentacaoId(anyLong())).thenReturn(false);
        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(distribuicaoSalvaDefault);

        useCase.executar(inputData);

        verify(movimentacaoItemDataProvider, times(1)).existePorMovimentacaoId(idMovimentacao);
        verify(movimentacaoItemDataProvider, times(0)).removerPorMovimentacao(idMovimentacao);
    }

    @Test
    public void naoDeveRemoverItensDaDistribuicaoQuandoRemoverOrgaoOrigemENaoPossuirItensAdicionados() {
        final Long orgaoOrigemId = 1L;
        final Long novoOrgaoOrigemId = null;

        final EditarDistribuicaoInputData inputData = EditarDistribuicaoInputData.builder()
            .id(idMovimentacao)
            .orgao(novoOrgaoOrigemId)
            .build();

        when(movimentacaoDataProvider.existe(anyLong())).thenReturn(true);
        when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(Movimentacao.builder()
                .id(idMovimentacao)
                .tipo(TipoMovimentacaoEnum.DISTRIBUICAO)
                .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                .orgaoOrigem(UnidadeOrganizacional.builder().id(orgaoOrigemId).build())
                .build()));
        when(movimentacaoItemDataProvider.existePorMovimentacaoId(anyLong())).thenReturn(false);
        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(distribuicaoSalvaDefault);

        useCase.executar(inputData);

        verify(movimentacaoItemDataProvider, times(1)).existePorMovimentacaoId(idMovimentacao);
        verify(movimentacaoItemDataProvider, times(0)).removerPorMovimentacao(idMovimentacao);
    }

    @Test
    public void deveRemoverItensDaDistribuicaoQuandoTrocarSetorOrigem() {
        final Long orgaoOrigemId = 1L;
        final Long setorOrigemId = 2L;
        final Long novoSetorOrigemId = 3L;

        final EditarDistribuicaoInputData inputData = EditarDistribuicaoInputData.builder()
            .id(idMovimentacao)
            .orgao(orgaoOrigemId)
            .setorOrigem(novoSetorOrigemId)
            .build();

        when(movimentacaoDataProvider.existe(anyLong())).thenReturn(true);
        when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(Movimentacao.builder()
                .id(idMovimentacao)
                .tipo(TipoMovimentacaoEnum.DISTRIBUICAO)
                .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                .orgaoOrigem(UnidadeOrganizacional.builder().id(orgaoOrigemId).build())
                .setorOrigem(UnidadeOrganizacional.builder().id(setorOrigemId).build())
                .build()));
        when(movimentacaoItemDataProvider.existePorMovimentacaoId(anyLong())).thenReturn(true);
        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(distribuicaoSalvaDefault);

        useCase.executar(inputData);

        verify(movimentacaoItemDataProvider, times(1)).existePorMovimentacaoId(idMovimentacao);
        verify(movimentacaoItemDataProvider, times(1)).removerPorMovimentacao(idMovimentacao);
    }

    @Test
    public void deveRemoverItensDaDistribuicaoQuandoRemoverSetorOrigem() {
        final Long orgaoOrigemId = 1L;
        final Long setorOrigemId = 2L;
        final Long novoSetorOrigemId = null;

        final EditarDistribuicaoInputData inputData = EditarDistribuicaoInputData.builder()
            .id(idMovimentacao)
            .orgao(orgaoOrigemId)
            .setorOrigem(novoSetorOrigemId)
            .build();

        final Movimentacao movimentacaoBusca = Movimentacao.builder()
            .id(idMovimentacao)
            .tipo(TipoMovimentacaoEnum.DISTRIBUICAO)
            .situacao(Movimentacao.Situacao.EM_ELABORACAO)
            .orgaoOrigem(UnidadeOrganizacional.builder().id(orgaoOrigemId).build())
            .setorOrigem(UnidadeOrganizacional.builder().id(setorOrigemId).build())
            .build();

        when(movimentacaoDataProvider.existe(anyLong())).thenReturn(true);
        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(movimentacaoBusca));
        when(movimentacaoItemDataProvider.existePorMovimentacaoId(anyLong())).thenReturn(true);
        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(distribuicaoSalvaDefault);

        useCase.executar(inputData);

        verify(movimentacaoItemDataProvider, times(1)).existePorMovimentacaoId(idMovimentacao);
        verify(movimentacaoItemDataProvider, times(1)).removerPorMovimentacao(idMovimentacao);
    }

    @Test
    public void naoDeveRemoverItensDaDistribuicaoQuandoTrocarSetorOrigemENaoPossuirItensAdicionados() {
        final Long orgaoOrigemId = 1L;
        final Long setorOrigemId = 2L;
        final Long novoSetorOrigemId = 3L;

        final EditarDistribuicaoInputData inputData = EditarDistribuicaoInputData.builder()
            .id(idMovimentacao)
            .orgao(orgaoOrigemId)
            .setorOrigem(novoSetorOrigemId)
            .build();

        final Movimentacao movimentacaoBusca = Movimentacao.builder()
            .id(idMovimentacao)
            .tipo(TipoMovimentacaoEnum.DISTRIBUICAO)
            .situacao(Movimentacao.Situacao.EM_ELABORACAO)
            .orgaoOrigem(UnidadeOrganizacional.builder().id(orgaoOrigemId).build())
            .setorOrigem(UnidadeOrganizacional.builder().id(setorOrigemId).build())
            .build();

        when(movimentacaoDataProvider.existe(anyLong())).thenReturn(true);
        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(movimentacaoBusca));
        when(movimentacaoItemDataProvider.existePorMovimentacaoId(anyLong())).thenReturn(false);
        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(distribuicaoSalvaDefault);

        useCase.executar(inputData);

        verify(movimentacaoItemDataProvider, times(1)).existePorMovimentacaoId(idMovimentacao);
        verify(movimentacaoItemDataProvider, times(0)).removerPorMovimentacao(idMovimentacao);
    }

    @Test
    public void naoDeveRemoverItensDaDistribuicaoQuandoRemoverSetorOrigemENaoPossuirItensAdicionados() {
        final Long orgaoOrigemId = 1L;
        final Long setorOrigemId = 2L;
        final Long novoSetorOrigemId = null;

        final EditarDistribuicaoInputData inputData = EditarDistribuicaoInputData.builder()
            .id(idMovimentacao)
            .orgao(orgaoOrigemId)
            .setorOrigem(novoSetorOrigemId)
            .build();

        final Movimentacao movimentacaoBusca = Movimentacao.builder()
            .id(idMovimentacao)
            .tipo(TipoMovimentacaoEnum.DISTRIBUICAO)
            .situacao(Movimentacao.Situacao.EM_ELABORACAO)
            .orgaoOrigem(UnidadeOrganizacional.builder().id(orgaoOrigemId).build())
            .setorOrigem(UnidadeOrganizacional.builder().id(setorOrigemId).build())
            .build();

        when(movimentacaoDataProvider.existe(anyLong())).thenReturn(true);
        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(movimentacaoBusca));
        when(movimentacaoItemDataProvider.existePorMovimentacaoId(anyLong())).thenReturn(false);
        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(distribuicaoSalvaDefault);

        useCase.executar(inputData);

        verify(movimentacaoItemDataProvider, times(1)).existePorMovimentacaoId(idMovimentacao);
        verify(movimentacaoItemDataProvider, times(0)).removerPorMovimentacao(idMovimentacao);
    }

    @Test
    public void devePreencherMotivoQuandoForInformado() {
        final String motivo = "Motivo";
        final EditarDistribuicaoInputData inputData = EditarDistribuicaoInputData.builder()
            .id(idMovimentacao)
            .motivoObservacao(motivo)
            .build();

        criarMocksTestePreenchimentoCampos();

        useCase.executar(inputData);

        verify(movimentacaoDataProvider, times(1)).buscarPorId(idMovimentacao);
        verify(movimentacaoDataProvider, times(1)).salvar(argumentCaptorMovimentacao.capture());
        assertNotNull(argumentCaptorMovimentacao.getValue().getId());
        assertNotNull(argumentCaptorMovimentacao.getValue().getMotivoObservacao());
        assertEquals(argumentCaptorMovimentacao.getValue().getMotivoObservacao(), motivo);
    }

    @Test
    public void devePreencherNumeroProcessoQuandoForInformado() {
        final String numeroProcesso = "2020NP2";
        final EditarDistribuicaoInputData inputData = EditarDistribuicaoInputData.builder()
            .id(idMovimentacao)
            .numeroProcesso(numeroProcesso)
            .build();

        criarMocksTestePreenchimentoCampos();

        useCase.executar(inputData);

        verify(movimentacaoDataProvider, times(1)).buscarPorId(idMovimentacao);
        verify(movimentacaoDataProvider, times(1)).salvar(argumentCaptorMovimentacao.capture());
        assertNotNull(argumentCaptorMovimentacao.getValue().getId());
        assertNotNull(argumentCaptorMovimentacao.getValue().getNumeroProcesso());
        assertEquals(argumentCaptorMovimentacao.getValue().getNumeroProcesso(), numeroProcesso);
    }

    @Test
    public void deveCriarUmaNotaLancamentoContabilQuandoPreencherONumeroENaoTiverNumeroEDataDaNotaPreenchidos() {
        final String numeroNotaLancamentoContabil = "2020NL123456";
        final EditarDistribuicaoInputData inputData = EditarDistribuicaoInputData.builder()
            .id(idMovimentacao)
            .numeroNotaLancamentoContabil(numeroNotaLancamentoContabil)
            .build();

        final NotaLancamentoContabil notaLancamentoContabilParaSalvar = NotaLancamentoContabil.builder()
            .numero(numeroNotaLancamentoContabil)
            .build();

        final NotaLancamentoContabil notaLancamentoContabilSalva = NotaLancamentoContabil.builder()
            .id(1L)
            .numero(numeroNotaLancamentoContabil)
            .build();

        criarMocksTestePreenchimentoCampos();
        when(notaLancamentoContabilDataProvider.existePorNumero(anyString())).thenReturn(false);
        when(notaLancamentoContabilDataProvider.salvar(any(NotaLancamentoContabil.class)))
            .thenReturn(notaLancamentoContabilSalva);

        useCase.executar(inputData);

        verify(movimentacaoDataProvider, times(1)).buscarPorId(idMovimentacao);
        verify(movimentacaoDataProvider, times(1)).salvar(argumentCaptorMovimentacao.capture());
        verify(notaLancamentoContabilDataProvider, times(1)).existePorNumero(numeroNotaLancamentoContabil);
        verify(notaLancamentoContabilDataProvider, times(1)).salvar(notaLancamentoContabilParaSalvar);
        assertEquals(argumentCaptorMovimentacao.getValue().getNotaLancamentoContabil(), notaLancamentoContabilSalva);
    }

    @Test
    public void deveCriarUmaNotaLancamentoContabilQuandoPreencherADataENaoTiverNumeroEDataDaNotaPreenchidos() {
        final Date dataNotaLancamentoContabil = DateUtils.asDate(dataNotaContabil);
        final EditarDistribuicaoInputData inputData = EditarDistribuicaoInputData.builder()
            .id(idMovimentacao)
            .dataNotaLancamentoContabil(dataNotaLancamentoContabil)
            .build();

        final NotaLancamentoContabil notaLancamentoContabilParaSalvar = NotaLancamentoContabil.builder()
            .dataLancamento(dataNotaContabil)
            .build();

        final NotaLancamentoContabil notaLancamentoContabilSalva = NotaLancamentoContabil.builder()
            .id(1L)
            .dataLancamento(dataNotaContabil)
            .build();

        criarMocksTestePreenchimentoCampos();
        when(notaLancamentoContabilDataProvider.salvar(any(NotaLancamentoContabil.class)))
            .thenReturn(notaLancamentoContabilSalva);

        useCase.executar(inputData);

        verify(movimentacaoDataProvider, times(1)).buscarPorId(idMovimentacao);
        verify(movimentacaoDataProvider, times(1)).salvar(argumentCaptorMovimentacao.capture());
        verify(notaLancamentoContabilDataProvider, times(1)).salvar(notaLancamentoContabilParaSalvar);
        assertEquals(argumentCaptorMovimentacao.getValue().getNotaLancamentoContabil(), notaLancamentoContabilSalva);
    }

    @Test
    public void deveAtualizarANotaLancamentoContabilExistenteQuandoAtualizarNumeroOuData() {
        final Long idNotaLancamentoContabil = 1L;
        final String numeroNotaLancamentoContabil = "2020NL123456";
        final String novoNumeroNotaLancamentoContabil = "2020NL123445";
        final EditarDistribuicaoInputData inputData = EditarDistribuicaoInputData.builder()
            .id(idMovimentacao)
            .numeroNotaLancamentoContabil(novoNumeroNotaLancamentoContabil)
            .build();

        final NotaLancamentoContabil notaLancamentoContabilParaSalvar = NotaLancamentoContabil.builder()
            .id(idNotaLancamentoContabil)
            .numero(novoNumeroNotaLancamentoContabil)
            .build();

        final NotaLancamentoContabil notaLancamentoContabilSalva = NotaLancamentoContabil.builder()
            .id(idNotaLancamentoContabil)
            .numero(novoNumeroNotaLancamentoContabil)
            .build();

        when(movimentacaoDataProvider.existe(anyLong())).thenReturn(true);
        when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(
                Movimentacao.builder()
                    .id(idMovimentacao)
                    .tipo(TipoMovimentacaoEnum.DISTRIBUICAO)
                    .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                    .notaLancamentoContabil(NotaLancamentoContabil.builder()
                        .id(idNotaLancamentoContabil)
                        .numero(numeroNotaLancamentoContabil)
                        .build())
                    .build()));

        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(distribuicaoSalvaDefault);
        when(notaLancamentoContabilDataProvider.existePorNumero(anyString())).thenReturn(false);
        when(notaLancamentoContabilDataProvider.salvar(any(NotaLancamentoContabil.class)))
            .thenReturn(notaLancamentoContabilSalva);

        useCase.executar(inputData);

        verify(movimentacaoDataProvider, times(1)).buscarPorId(idMovimentacao);
        verify(movimentacaoDataProvider, times(1)).salvar(argumentCaptorMovimentacao.capture());
        verify(notaLancamentoContabilDataProvider, times(1)).existePorNumero(novoNumeroNotaLancamentoContabil);
        verify(notaLancamentoContabilDataProvider, times(1)).salvar(notaLancamentoContabilParaSalvar);
        assertEquals(argumentCaptorMovimentacao.getValue().getNotaLancamentoContabil(), notaLancamentoContabilSalva);
    }

    @Test
    public void deveRemoverANotaLancamentoContabilQuandoOsCamposNumeroEDataForemNulos() {
        final Long idNotaLancamentoContabil = 1L;
        final EditarDistribuicaoInputData inputData = EditarDistribuicaoInputData.builder()
            .id(idMovimentacao)
            .build();

        when(movimentacaoDataProvider.existe(anyLong())).thenReturn(true);
        when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(
                Movimentacao.builder()
                    .id(idMovimentacao)
                    .tipo(TipoMovimentacaoEnum.DISTRIBUICAO)
                    .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                    .notaLancamentoContabil(NotaLancamentoContabil.builder()
                        .id(idNotaLancamentoContabil)
                        .numero("2020NL123456")
                        .build())
                    .build()));
        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(distribuicaoSalvaDefault);

        useCase.executar(inputData);

        verify(notaLancamentoContabilDataProvider, times(1)).remover(idNotaLancamentoContabil);
        verify(movimentacaoDataProvider, times(1)).salvar(argumentCaptorMovimentacao.capture());
        assertNull(argumentCaptorMovimentacao.getValue().getNotaLancamentoContabil());
    }

    @Test
    public void naoDeveCriarNotaLancamentoContabilQuandoOsCamposForemNulos() {
        final EditarDistribuicaoInputData inputData = EditarDistribuicaoInputData.builder()
            .id(idMovimentacao)
            .build();

        criarMocksTestePreenchimentoCampos();

        useCase.executar(inputData);

        verifyZeroInteractions(notaLancamentoContabilDataProvider);
    }

    @Test
    public void devePermitirEditarQuandoSituacaoForErroProcessamento() {
        final EditarDistribuicaoInputData inputData = EditarDistribuicaoInputData.builder()
            .id(idMovimentacao)
            .numeroProcesso("1234")
            .build();

        final Movimentacao distribuicao = Movimentacao.builder()
            .id(idMovimentacao)
            .tipo(TipoMovimentacaoEnum.DISTRIBUICAO)
            .situacao(Movimentacao.Situacao.ERRO_PROCESSAMENTO)
            .build();

        final Movimentacao distribuicaoSalva = Movimentacao.builder()
            .id(idMovimentacao)
            .tipo(TipoMovimentacaoEnum.DISTRIBUICAO)
            .situacao(Movimentacao.Situacao.ERRO_PROCESSAMENTO)
            .numeroProcesso("1234")
            .responsavel(Responsavel.builder().build())
            .build();

        when(movimentacaoDataProvider.existe(anyLong())).thenReturn(true);
        when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(distribuicao));

        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(distribuicaoSalvaDefault);

        useCase.executar(inputData);

        verify(movimentacaoDataProvider, times(1)).salvar(distribuicaoSalva);
    }

    private void criarMocksTestePreenchimentoCampos() {
        final Movimentacao movimentacao = Movimentacao.builder()
            .id(idMovimentacao)
            .tipo(TipoMovimentacaoEnum.DISTRIBUICAO)
            .situacao(Movimentacao.Situacao.EM_ELABORACAO)
            .build();

        when(movimentacaoItemDataProvider.existePorMovimentacaoId(anyLong())).thenReturn(false);
        when(movimentacaoDataProvider.existe(anyLong())).thenReturn(true);
        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(movimentacao));
        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(distribuicaoSalvaDefault);
    }
}
