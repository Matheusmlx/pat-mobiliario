package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizarerro;

import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.Notificacao;
import br.com.azi.patrimoniomobiliario.domain.entity.Usuario;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotificacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.UsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizarerro.exception.DistribuicaoNaoEncontradaException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FinalizarDistribuicaoErroUseCaseTest {

    private final static LocalDate LOCAL_DATE = LocalDate.of(2021, 1, 12);
    private final Clock fixedClock = Clock.fixed(LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

    @Mock
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Mock
    private NotificacaoDataProvider notificacaoDataProvider;

    @Mock
    private UsuarioDataProvider usuarioDataProvider;

    private FinalizarDistribuicaoErroUseCase useCase;

    @Before
    public void inicializar() {
        useCase = new FinalizarDistribuicaoErroUseCaseImpl(
            movimentacaoDataProvider,
            notificacaoDataProvider,
            usuarioDataProvider,
            fixedClock
        );
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoIdMovimentacaoForNulo() {
        useCase.executar(FinalizarDistribuicaoErroInputData.builder().build());
    }

    @Test(expected = DistribuicaoNaoEncontradaException.class)
    public void deveFalharQuandoNaoEncontrarDistribuicao() {
        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.empty());

        useCase.executar(FinalizarDistribuicaoErroInputData.builder()
            .movimentacaoId(1L)
            .build());
    }

    @Test
    public void deveAtualizarADistribuicaoCOmInformacoesDeErroNoProcessamentoQuandoHouverAlgumErroNoProcessamento() {
        final long movimentacaoId = 1L;
        final String erroProcessamento = "Ocorreu um erro ao processar a finalização da distribuição.";
        final Movimentacao movimentacao = Movimentacao.builder()
            .id(movimentacaoId)
            .situacao(Movimentacao.Situacao.EM_PROCESSAMENTO)
            .build();

        final Movimentacao movimentacaoSalva = Movimentacao.builder()
            .id(movimentacaoId)
            .situacao(Movimentacao.Situacao.ERRO_PROCESSAMENTO)
            .dataFimProcessamento(LOCAL_DATE.atStartOfDay())
            .erroProcessamento(erroProcessamento)
            .build();

        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(movimentacao));
        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(movimentacaoSalva);

        useCase.executar(FinalizarDistribuicaoErroInputData.builder()
            .movimentacaoId(movimentacaoId)
            .erroProcessamento(erroProcessamento)
            .build());

        verify(movimentacaoDataProvider, times(1)).salvar(movimentacaoSalva);
    }

    @Test
    public void deveGerarNotificacaoDeErroNoProcessamentoParaUsuarioQueFinalizouDistribuicao() {
        final long movimentacaoId = 1L;
        final String erroProcessamento = "Ocorreu um erro ao processar a finalização da distribuição.";
        final Movimentacao movimentacao = Movimentacao.builder()
            .id(movimentacaoId)
            .situacao(Movimentacao.Situacao.EM_PROCESSAMENTO)
            .build();

        final Movimentacao movimentacaoSalva = Movimentacao.builder()
            .id(movimentacaoId)
            .situacao(Movimentacao.Situacao.ERRO_PROCESSAMENTO)
            .dataFimProcessamento(LOCAL_DATE.atStartOfDay())
            .erroProcessamento(erroProcessamento)
            .build();

        final Usuario usuario = Usuario.builder().id(1L).build();

        final Notificacao notificacao = Notificacao.builder()
            .origem(Notificacao.Origem.DISTRIBUICAO)
            .origemId(movimentacao.getId())
            .assunto("Distribuição " + movimentacao.getCodigo())
            .mensagem("Erro no processamento")
            .dataCriacao(LOCAL_DATE.atStartOfDay())
            .visualizada(Boolean.FALSE)
            .usuario(usuario)
            .build();

        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(movimentacao));
        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(movimentacaoSalva);
        when(usuarioDataProvider.buscarUsuarioPorLogin(movimentacao.getUsuarioFinalizacao())).thenReturn(usuario);

        useCase.executar(FinalizarDistribuicaoErroInputData.builder()
            .movimentacaoId(movimentacaoId)
            .erroProcessamento(erroProcessamento)
            .build());

        verify(notificacaoDataProvider, times(1)).salvar(notificacao);
    }

    @Test
    public void deveAtualizarNotificacaoAnteriorParaErroNoProcessamentoParaUsuarioQueFinalizouDistribuicao() {
        final long movimentacaoId = 1L;
        final String erroProcessamento = "Ocorreu um erro ao processar a finalização da distribuição.";
        final Movimentacao movimentacao = Movimentacao.builder()
            .id(movimentacaoId)
            .situacao(Movimentacao.Situacao.EM_PROCESSAMENTO)
            .build();

        final Movimentacao movimentacaoSalva = Movimentacao.builder()
            .id(movimentacaoId)
            .situacao(Movimentacao.Situacao.ERRO_PROCESSAMENTO)
            .dataFimProcessamento(LOCAL_DATE.atStartOfDay())
            .erroProcessamento(erroProcessamento)
            .build();

        final Usuario usuario = Usuario.builder().id(1L).build();

        final Notificacao notificacao = Notificacao.builder()
            .origem(Notificacao.Origem.DISTRIBUICAO)
            .origemId(movimentacao.getId())
            .assunto("Distribuição " + movimentacao.getCodigo())
            .mensagem("Erro no processamento")
            .dataCriacao(LOCAL_DATE.atStartOfDay())
            .visualizada(Boolean.FALSE)
            .usuario(usuario)
            .build();

        final Notificacao notificacaoAnterior = Notificacao.builder()
            .origem(Notificacao.Origem.DISTRIBUICAO)
            .origemId(movimentacao.getId())
            .assunto("Distribuição " + movimentacao.getCodigo())
            .mensagem("Em elaboração")
            .dataCriacao(LOCAL_DATE.atStartOfDay())
            .visualizada(Boolean.FALSE)
            .usuario(usuario)
            .build();

        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(movimentacao));
        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(movimentacaoSalva);
        when(usuarioDataProvider.buscarUsuarioPorLogin(movimentacao.getUsuarioFinalizacao())).thenReturn(usuario);
        when(notificacaoDataProvider.buscarNotificacaoPorOrigemEOrigemId(anyString(), anyLong()))
            .thenReturn(notificacaoAnterior);

        useCase.executar(FinalizarDistribuicaoErroInputData.builder()
            .movimentacaoId(movimentacaoId)
            .erroProcessamento(erroProcessamento)
            .build());

        verify(notificacaoDataProvider, times(1)).salvar(notificacao);
    }

    @Test
    public void naoDeveGerarNotificacaoQuandoNaoEncontrarUsuarioQueFinalizouDistribuicao() {
        final long movimentacaoId = 1L;
        final String erroProcessamento = "Ocorreu um erro ao processar a finalização da distribuição.";
        final Movimentacao movimentacao = Movimentacao.builder()
            .id(movimentacaoId)
            .situacao(Movimentacao.Situacao.EM_PROCESSAMENTO)
            .build();

        final Movimentacao movimentacaoSalva = Movimentacao.builder()
            .id(movimentacaoId)
            .situacao(Movimentacao.Situacao.ERRO_PROCESSAMENTO)
            .dataFimProcessamento(LOCAL_DATE.atStartOfDay())
            .erroProcessamento(erroProcessamento)
            .build();

        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(movimentacao));
        when(movimentacaoDataProvider.salvar(any(Movimentacao.class))).thenReturn(movimentacaoSalva);
        when(usuarioDataProvider.buscarUsuarioPorLogin(movimentacao.getUsuarioFinalizacao())).thenReturn(null);

        useCase.executar(FinalizarDistribuicaoErroInputData.builder()
            .movimentacaoId(movimentacaoId)
            .erroProcessamento(erroProcessamento)
            .build());

        verifyZeroInteractions(notificacaoDataProvider);
    }
}
