package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizarerro;

import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.Notificacao;
import br.com.azi.patrimoniomobiliario.domain.entity.Usuario;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotificacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.UsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizarerro.exception.IncorporacaoNaoEncontradaException;
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
public class FinalizarIncorporacaoErroUseCaseTest {

    private final static LocalDate LOCAL_DATE = LocalDate.of(2021, 1, 12);

    private final Clock fixedClock = Clock.fixed(LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

    @Mock
    private IncorporacaoDataProvider incorporacaoDataProvider;

    @Mock
    private NotificacaoDataProvider notificacaoDataProvider;

    @Mock
    private UsuarioDataProvider usuarioDataProvider;

    private FinalizarIncorporacaoErroUseCase useCase;

    @Before
    public void inicializar() {
        useCase = new FinalizarIncorporacaoErroUseCaseImpl(
            incorporacaoDataProvider,
            notificacaoDataProvider,
            usuarioDataProvider,
            fixedClock
        );
    }

    @Test(expected = IncorporacaoNaoEncontradaException.class)
    public void deveFalharQuandoNaoEncontrarAIncorporacaoPorId() {
        final FinalizarIncorporacaoErroInputData inputData = FinalizarIncorporacaoErroInputData.builder()
            .incorporacaoId(1L)
            .build();

        when(incorporacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.empty());

        useCase.executar(inputData);
    }

    @Test
    public void deveAtualizarIncorporacaoComInformacoesDeErroQuandoOcorrerUmErroNoProcessamento() {
        final FinalizarIncorporacaoErroInputData inputData = FinalizarIncorporacaoErroInputData.builder()
            .incorporacaoId(1L)
            .erroProcessamento("Ocorreu um erro no processamento")
            .build();

        final Incorporacao incorporacao = Incorporacao.builder()
            .id(1L)
            .situacao(Incorporacao.Situacao.EM_PROCESSAMENTO)
            .build();

        final Incorporacao incorporacaoAtualizada = Incorporacao.builder()
            .id(1L)
            .situacao(Incorporacao.Situacao.ERRO_PROCESSAMENTO)
            .dataFimProcessamento(LOCAL_DATE.atStartOfDay())
            .erroProcessamento(inputData.getErroProcessamento())
            .build();

        when(incorporacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(incorporacao));
        when(incorporacaoDataProvider.salvar(any(Incorporacao.class))).thenReturn(incorporacaoAtualizada);

        useCase.executar(inputData);

        verify(incorporacaoDataProvider, times(1)).salvar(incorporacaoAtualizada);
    }

    @Test
    public void deveGerarNotificacaoDeErroQuandoOcorrerUmErroNoProcessamento() {
        final FinalizarIncorporacaoErroInputData inputData = FinalizarIncorporacaoErroInputData.builder()
            .incorporacaoId(1L)
            .erroProcessamento("Ocorreu um erro no processamento")
            .build();

        final String usuarioFinalizacaoLogin = "admin";
        final Usuario usuarioFinalizacao = Usuario.builder().id(1L).build();

        final Incorporacao incorporacao = Incorporacao.builder()
            .id(1L)
            .situacao(Incorporacao.Situacao.EM_PROCESSAMENTO)
            .usuarioFinalizacao(usuarioFinalizacaoLogin)
            .build();

        final Incorporacao incorporacaoAtualizada = Incorporacao.builder()
            .id(1L)
            .situacao(Incorporacao.Situacao.ERRO_PROCESSAMENTO)
            .usuarioFinalizacao(usuarioFinalizacaoLogin)
            .dataFimProcessamento(LOCAL_DATE.atStartOfDay())
            .erroProcessamento(inputData.getErroProcessamento())
            .build();

        final Notificacao notificacao = Notificacao.builder()
            .origem(Notificacao.Origem.INCORPORACAO)
            .origemId(incorporacao.getId())
            .assunto("Incorporação " + incorporacao.getCodigo())
            .mensagem("Erro no processamento")
            .dataCriacao(LOCAL_DATE.atStartOfDay())
            .visualizada(Boolean.FALSE)
            .usuario(usuarioFinalizacao)
            .build();

        when(incorporacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(incorporacao));
        when(incorporacaoDataProvider.salvar(any(Incorporacao.class))).thenReturn(incorporacaoAtualizada);
        when(usuarioDataProvider.buscarUsuarioPorLogin(usuarioFinalizacaoLogin)).thenReturn(usuarioFinalizacao);

        useCase.executar(inputData);

        verify(notificacaoDataProvider, times(1)).salvar(notificacao);
    }

    @Test
    public void deveAtualizarNotificacaoAnteriorParaErroQuandoOcorrerUmErroNoProcessamento() {
        final FinalizarIncorporacaoErroInputData inputData = FinalizarIncorporacaoErroInputData.builder()
            .incorporacaoId(1L)
            .erroProcessamento("Ocorreu um erro no processamento")
            .build();

        final String usuarioFinalizacaoLogin = "admin";
        final Usuario usuarioFinalizacao = Usuario.builder().id(1L).build();

        final Incorporacao incorporacao = Incorporacao.builder()
            .id(1L)
            .situacao(Incorporacao.Situacao.EM_PROCESSAMENTO)
            .usuarioFinalizacao(usuarioFinalizacaoLogin)
            .build();

        final Incorporacao incorporacaoAtualizada = Incorporacao.builder()
            .id(1L)
            .situacao(Incorporacao.Situacao.ERRO_PROCESSAMENTO)
            .usuarioFinalizacao(usuarioFinalizacaoLogin)
            .dataFimProcessamento(LOCAL_DATE.atStartOfDay())
            .erroProcessamento(inputData.getErroProcessamento())
            .build();

        final Notificacao notificacao = Notificacao.builder()
            .origem(Notificacao.Origem.INCORPORACAO)
            .origemId(incorporacao.getId())
            .assunto("Incorporação " + incorporacao.getCodigo())
            .mensagem("Erro no processamento")
            .dataCriacao(LOCAL_DATE.atStartOfDay())
            .visualizada(Boolean.FALSE)
            .usuario(usuarioFinalizacao)
            .build();

        final Notificacao notificacaoAnterior = Notificacao.builder()
            .origem(Notificacao.Origem.INCORPORACAO)
            .origemId(incorporacao.getId())
            .assunto("Incorporação " + incorporacao.getCodigo())
            .mensagem("Em elaboração")
            .dataCriacao(LOCAL_DATE.atStartOfDay())
            .visualizada(Boolean.FALSE)
            .usuario(usuarioFinalizacao)
            .build();

        when(incorporacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(incorporacao));
        when(incorporacaoDataProvider.salvar(any(Incorporacao.class))).thenReturn(incorporacaoAtualizada);
        when(usuarioDataProvider.buscarUsuarioPorLogin(usuarioFinalizacaoLogin)).thenReturn(usuarioFinalizacao);
        when(notificacaoDataProvider.buscarNotificacaoPorOrigemEOrigemId(anyString(), anyLong()))
            .thenReturn(notificacaoAnterior);

        useCase.executar(inputData);

        verify(notificacaoDataProvider, times(1)).salvar(notificacao);
    }

    @Test
    public void naoDeveGerarNotificacaoDeErroQuandoNaoEncontrarOUsuarioDeFinalizacaoDaIncorporacao() {
        final FinalizarIncorporacaoErroInputData inputData = FinalizarIncorporacaoErroInputData.builder()
            .incorporacaoId(1L)
            .erroProcessamento("Ocorreu um erro no processamento")
            .build();

        final String usuarioFinalizacaoLogin = "admin";

        final Incorporacao incorporacao = Incorporacao.builder()
            .id(1L)
            .situacao(Incorporacao.Situacao.EM_PROCESSAMENTO)
            .usuarioFinalizacao(usuarioFinalizacaoLogin)
            .build();

        final Incorporacao incorporacaoAtualizada = Incorporacao.builder()
            .id(1L)
            .situacao(Incorporacao.Situacao.ERRO_PROCESSAMENTO)
            .usuarioFinalizacao(usuarioFinalizacaoLogin)
            .dataFimProcessamento(LOCAL_DATE.atStartOfDay())
            .erroProcessamento(inputData.getErroProcessamento())
            .build();

        when(incorporacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.of(incorporacao));
        when(incorporacaoDataProvider.salvar(any(Incorporacao.class))).thenReturn(incorporacaoAtualizada);
        when(usuarioDataProvider.buscarUsuarioPorLogin(usuarioFinalizacaoLogin)).thenReturn(null);

        useCase.executar(inputData);

        verifyZeroInteractions(notificacaoDataProvider);
    }
}
