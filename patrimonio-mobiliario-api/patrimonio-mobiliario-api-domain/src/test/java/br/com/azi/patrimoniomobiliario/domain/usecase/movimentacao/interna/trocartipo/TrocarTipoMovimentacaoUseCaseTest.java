package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.trocartipo;

import br.com.azi.patrimoniomobiliario.domain.constant.movimentacao.TipoMovimentacaoEnum;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.NotaLancamentoContabil;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotaLancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.trocartipo.converter.TrocarTipoMovimentacaoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.trocartipo.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.trocartipo.exception.MovimentacaoNaoEstaEmElaboracaoException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TrocarTipoMovimentacaoUseCaseTest {

    private final Long movimentacaoId = 1L;

    private final String usuarioLogin = "usuarioLogin";

    @Mock
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Mock
    private MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    @Mock
    private DocumentoDataProvider documentoDataProvider;

    @Mock
    private SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    @Mock
    private NotaLancamentoContabilDataProvider notaLancamentoContabilDataProvider;

    private TrocarTipoMovimentacaoUseCase useCase;

    private Movimentacao movimentacaoExistente;

    private Movimentacao novaMovimentacaoSalva;

    private TrocarTipoMovimentacaoInputData inputDataSucesso;

    @Before
    public void inicializar() {
        useCase = new TrocarTipoMovimentacaoUseCaseImpl(
            movimentacaoDataProvider,
            movimentacaoItemDataProvider,
            documentoDataProvider,
            sessaoUsuarioDataProvider,
            notaLancamentoContabilDataProvider,
            new TrocarTipoMovimentacaoOutputDataConverter()
        );

        movimentacaoExistente = Movimentacao.builder()
            .id(movimentacaoId)
            .situacao(Movimentacao.Situacao.EM_ELABORACAO)
            .tipo(TipoMovimentacaoEnum.DISTRIBUICAO)
            .build();

        novaMovimentacaoSalva = Movimentacao.builder()
            .id(2L)
            .tipo(TipoMovimentacaoEnum.ENTRE_SETORES)
            .situacao(Movimentacao.Situacao.EM_ELABORACAO)
            .codigo("00002")
            .usuarioCriacao(usuarioLogin)
            .build();

        inputDataSucesso = TrocarTipoMovimentacaoInputData
            .builder()
            .id(movimentacaoId)
            .tipo("ENTRE_SETORES")
            .build();
    }

    @Test(expected = IllegalStateException.class)
    public void deveLancarExcecaoQuandoOIdDaMovimentacaoForNulo() {
        useCase.executar(new TrocarTipoMovimentacaoInputData());
    }

    @Test(expected = IllegalStateException.class)
    public void deveLancarExcecaoQuandoONovoTipoDaMovimentacaoForNulo() {
        useCase.executar(TrocarTipoMovimentacaoInputData.builder()
            .id(movimentacaoId)
            .build());
    }

    @Test(expected = MovimentacaoNaoEncontradaException.class)
    public void deveLancarExcecaoQuandoNaoEncontrarAMovimentacaoPeloId() {
        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.empty());

        useCase.executar(TrocarTipoMovimentacaoInputData.builder()
            .id(movimentacaoId)
            .tipo("DISTRIBUICAO")
            .build());

        verify(movimentacaoDataProvider, times(1)).buscarPorId(movimentacaoId);
    }

    @Test(expected = MovimentacaoNaoEstaEmElaboracaoException.class)
    public void deveLancarExcecaoQuandoAMovimentacaoNaoEstiverEmElaboracao() {
        movimentacaoExistente.setSituacao(Movimentacao.Situacao.FINALIZADO);

        when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(movimentacaoExistente));

        useCase.executar(TrocarTipoMovimentacaoInputData.builder()
            .id(movimentacaoId)
            .tipo("DISTRIBUICAO")
            .build());
    }

    @Test
    public void deveRemoverAMovimentacaoAntigaECriarOutraComONovoTipoQuandoAlterarOTipo() {
        final Movimentacao novaMovimentacao = Movimentacao.builder()
            .tipo(TipoMovimentacaoEnum.ENTRE_SETORES)
            .situacao(Movimentacao.Situacao.EM_ELABORACAO)
            .codigo("00002")
            .usuarioCriacao(usuarioLogin)
            .build();

        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(
            Movimentacao.builder()
                .id(movimentacaoId)
                .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                .tipo(TipoMovimentacaoEnum.DISTRIBUICAO)
                .build()));
        when(movimentacaoDataProvider.buscarUltimoCodigoCadastrado()).thenReturn("00001");
        when(sessaoUsuarioDataProvider.getLogin()).thenReturn(usuarioLogin);
        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(novaMovimentacaoSalva);

        useCase.executar(inputDataSucesso);

        verify(movimentacaoDataProvider, times(1)).remover(movimentacaoExistente);
        verify(movimentacaoDataProvider, times(1)).salvar(novaMovimentacao);
    }

    @Test
    public void deveRemoverOsItensDaMovimentacaoQuandoTrocarOTipoDaMovimentacaoEHouverItensVinculados() {
        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(
            Movimentacao.builder()
                .id(movimentacaoId)
                .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                .tipo(TipoMovimentacaoEnum.DISTRIBUICAO)
                .build()));
        when(movimentacaoDataProvider.buscarUltimoCodigoCadastrado()).thenReturn("00001");
        when(sessaoUsuarioDataProvider.getLogin()).thenReturn(usuarioLogin);
        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(novaMovimentacaoSalva);
        when(movimentacaoItemDataProvider.existePorMovimentacaoId(anyLong())).thenReturn(true);

        useCase.executar(inputDataSucesso);

        verify(movimentacaoItemDataProvider, times(1)).existePorMovimentacaoId(movimentacaoId);
        verify(movimentacaoItemDataProvider, times(1)).removerPorMovimentacao(movimentacaoId);
    }

    @Test
    public void naoDeveRemoverOsItensDaMovimentacaoQuandoTrocarOTipoDaMovimentacaoENaoHouverItensVinculados() {
        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(
            Movimentacao.builder()
                .id(movimentacaoId)
                .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                .tipo(TipoMovimentacaoEnum.DISTRIBUICAO)
                .build()));
        when(movimentacaoDataProvider.buscarUltimoCodigoCadastrado()).thenReturn("00001");
        when(sessaoUsuarioDataProvider.getLogin()).thenReturn(usuarioLogin);
        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(novaMovimentacaoSalva);
        when(movimentacaoItemDataProvider.existePorMovimentacaoId(anyLong())).thenReturn(false);

        useCase.executar(inputDataSucesso);

        verify(movimentacaoItemDataProvider, times(1)).existePorMovimentacaoId(movimentacaoId);
        verify(movimentacaoItemDataProvider, times(0)).removerPorMovimentacao(movimentacaoId);
    }

    @Test
    public void deveRemoverOsDocumentosDaMovimentacaoQuandoTrocarOTipoDaMovimentacaoEHouverDocumentosVinculados() {
        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(
            Movimentacao.builder()
                .id(movimentacaoId)
                .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                .tipo(TipoMovimentacaoEnum.DISTRIBUICAO)
                .build()));
        when(movimentacaoDataProvider.buscarUltimoCodigoCadastrado()).thenReturn("00001");
        when(sessaoUsuarioDataProvider.getLogin()).thenReturn(usuarioLogin);
        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(novaMovimentacaoSalva);
        when(documentoDataProvider.existePorMovimentacaoId(anyLong())).thenReturn(true);

        useCase.executar(inputDataSucesso);

        verify(documentoDataProvider, times(1)).existePorMovimentacaoId(movimentacaoId);
        verify(documentoDataProvider, times(1)).removerPorMovimentacao(movimentacaoId);
    }

    @Test
    public void naoDeveRemoverOsDocumentosDaMovimentacaoQuandoTrocarOTipoDaMovimentacaoENaoHouverDocumentosVinculados() {
        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(
            Movimentacao.builder()
                .id(movimentacaoId)
                .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                .tipo(TipoMovimentacaoEnum.DISTRIBUICAO)
                .build()));
        when(movimentacaoDataProvider.buscarUltimoCodigoCadastrado()).thenReturn("00001");
        when(sessaoUsuarioDataProvider.getLogin()).thenReturn(usuarioLogin);
        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(novaMovimentacaoSalva);
        when(documentoDataProvider.existePorMovimentacaoId(anyLong())).thenReturn(false);

        useCase.executar(inputDataSucesso);

        verify(documentoDataProvider, times(1)).existePorMovimentacaoId(movimentacaoId);
        verify(documentoDataProvider, times(0)).removerPorMovimentacao(movimentacaoId);
    }

    @Test
    public void deveRemoverANotaLancamentoContabilQuandoTrocarOTipoDeMovimentacao() {
        movimentacaoExistente.setNotaLancamentoContabil(NotaLancamentoContabil.builder()
            .id(1L)
            .numero("2020NL12345")
            .build());

        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(movimentacaoExistente));
        when(movimentacaoDataProvider.buscarUltimoCodigoCadastrado()).thenReturn("00001");
        when(sessaoUsuarioDataProvider.getLogin()).thenReturn(usuarioLogin);
        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(novaMovimentacaoSalva);

        useCase.executar(inputDataSucesso);

        verify(notaLancamentoContabilDataProvider, times(1))
            .remover(movimentacaoExistente.getNotaLancamentoContabil().getId());
    }

    @Test
    public void naoDeveRemoverANotaLancamentoContabilQuandoTrocarOTipoDeMovimentacaoENaoTiverNotaLancamentoContabilVinculada() {
        movimentacaoExistente.setNotaLancamentoContabil(null);

        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(movimentacaoExistente));
        when(movimentacaoDataProvider.buscarUltimoCodigoCadastrado()).thenReturn("00001");
        when(sessaoUsuarioDataProvider.getLogin()).thenReturn(usuarioLogin);
        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(novaMovimentacaoSalva);

        useCase.executar(inputDataSucesso);

        verifyZeroInteractions(notaLancamentoContabilDataProvider);
    }

    @Test
    public void deveRetornarOsDadosDaNovaMovimentacaoQuandoTrocarOTipoDeMovimentacao() {
        final TrocarTipoMovimentacaoOutputData outputDataEsperado = TrocarTipoMovimentacaoOutputData.builder()
            .id(novaMovimentacaoSalva.getId())
            .codigo(novaMovimentacaoSalva.getCodigo())
            .situacao(novaMovimentacaoSalva.getSituacao().name())
            .tipo(novaMovimentacaoSalva.getTipo().name())
            .usuarioCriacao(novaMovimentacaoSalva.getUsuarioCriacao())
            .build();

        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(
            Movimentacao.builder()
                .id(movimentacaoId)
                .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                .tipo(TipoMovimentacaoEnum.DISTRIBUICAO)
                .build()));
        when(movimentacaoDataProvider.buscarUltimoCodigoCadastrado()).thenReturn("00001");
        when(sessaoUsuarioDataProvider.getLogin()).thenReturn(usuarioLogin);
        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(novaMovimentacaoSalva);

        final TrocarTipoMovimentacaoOutputData outputData = useCase.executar(inputDataSucesso);

        assertEquals(outputData, outputDataEsperado);
    }

    @Test
    public void naoDeveTrocarOTipoDeMovimentacaoQuandoOTipoEscolhidoForIgualAoTipoAtualDaMovimentacao() {
        inputDataSucesso.setTipo(TipoMovimentacaoEnum.DISTRIBUICAO.name());
        movimentacaoExistente.setTipo(TipoMovimentacaoEnum.DISTRIBUICAO);

        final TrocarTipoMovimentacaoOutputData outputDataEsperado = TrocarTipoMovimentacaoOutputData.builder()
            .id(movimentacaoExistente.getId())
            .codigo(movimentacaoExistente.getCodigo())
            .situacao(movimentacaoExistente.getSituacao().name())
            .tipo(movimentacaoExistente.getTipo().name())
            .usuarioCriacao(movimentacaoExistente.getUsuarioCriacao())
            .build();

        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(
            Movimentacao.builder()
                .id(movimentacaoId)
                .situacao(Movimentacao.Situacao.EM_ELABORACAO)
                .tipo(TipoMovimentacaoEnum.DISTRIBUICAO)
                .build()));

        final TrocarTipoMovimentacaoOutputData outputData = useCase.executar(inputDataSucesso);

        assertEquals(outputData, outputDataEsperado);
        verify(movimentacaoDataProvider, times(0)).buscarUltimoCodigoCadastrado();
        verify(movimentacaoDataProvider, times(0)).remover(any(Movimentacao.class));
        verify(movimentacaoDataProvider, times(0)).salvar(any(Movimentacao.class));
    }

    @Test
    public void deveRetornarOsDadosDaNovaMovimentacaoQuandoTrocarOTipoDeMovimentacaoEMovimentacaoAnteriorEstiverComErroDeProcessamento() {
        final TrocarTipoMovimentacaoOutputData outputDataEsperado = TrocarTipoMovimentacaoOutputData.builder()
            .id(novaMovimentacaoSalva.getId())
            .codigo(novaMovimentacaoSalva.getCodigo())
            .situacao(novaMovimentacaoSalva.getSituacao().name())
            .tipo(novaMovimentacaoSalva.getTipo().name())
            .usuarioCriacao(novaMovimentacaoSalva.getUsuarioCriacao())
            .build();

        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(
            Movimentacao.builder()
                .id(movimentacaoId)
                .situacao(Movimentacao.Situacao.ERRO_PROCESSAMENTO)
                .tipo(TipoMovimentacaoEnum.DISTRIBUICAO)
                .build()));
        when(movimentacaoDataProvider.buscarUltimoCodigoCadastrado()).thenReturn("00001");
        when(sessaoUsuarioDataProvider.getLogin()).thenReturn(usuarioLogin);
        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(novaMovimentacaoSalva);

        final TrocarTipoMovimentacaoOutputData outputData = useCase.executar(inputDataSucesso);

        assertEquals(outputData, outputDataEsperado);
    }
}
