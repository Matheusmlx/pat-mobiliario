package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizarerro;

import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.Notificacao;
import br.com.azi.patrimoniomobiliario.domain.entity.Usuario;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotificacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.UsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizarerro.exception.IncorporacaoNaoEncontradaException;
import lombok.AllArgsConstructor;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
public class FinalizarIncorporacaoErroUseCaseImpl implements FinalizarIncorporacaoErroUseCase {

    private final IncorporacaoDataProvider incorporacaoDataProvider;

    private final NotificacaoDataProvider notificacaoDataProvider;

    private final UsuarioDataProvider usuarioDataProvider;

    private final Clock clock;

    @Override
    public void executar(FinalizarIncorporacaoErroInputData inputData) {
        final Incorporacao incorporacao = buscarIncorporacao(inputData.getIncorporacaoId());

        atualizarIncorporacaoComErroProcessamento(incorporacao, inputData);
        final Incorporacao incorporacaoComErro = salvarIncorporacao(incorporacao);

        notificarErroProcessamentoFinalizacaoIncorporacao(incorporacaoComErro);
    }

    private Incorporacao buscarIncorporacao(Long id) {
        return incorporacaoDataProvider.buscarPorId(id)
            .orElseThrow(IncorporacaoNaoEncontradaException::new);
    }

    private void atualizarIncorporacaoComErroProcessamento(Incorporacao incorporacao, FinalizarIncorporacaoErroInputData inputData) {
        incorporacao.setSituacao(Incorporacao.Situacao.ERRO_PROCESSAMENTO);
        incorporacao.setDataFimProcessamento(LocalDateTime.now(clock));
        incorporacao.setErroProcessamento(inputData.getErroProcessamento());
    }

    private Incorporacao salvarIncorporacao(Incorporacao incorporacao) {
        return incorporacaoDataProvider.salvar(incorporacao);
    }

    private void notificarErroProcessamentoFinalizacaoIncorporacao(Incorporacao incorporacao) {
        final Usuario usuario = buscarUsuarioFinalizacao(incorporacao.getUsuarioFinalizacao());

        if (Objects.nonNull(usuario)) {
            final Notificacao.Origem origem = Notificacao.Origem.INCORPORACAO;
            final Notificacao notificacao = buscarNotificacaoPorOrigemEOrigemId(origem, incorporacao.getId());

            if (Objects.nonNull(notificacao)) {
                atualizarNotificacao(notificacao);
            } else {
                criarNotificacao(incorporacao, usuario);
            }
        }
    }

    private Usuario buscarUsuarioFinalizacao(String usuarioFinalizacao) {
        return usuarioDataProvider.buscarUsuarioPorLogin(usuarioFinalizacao);
    }

    private Notificacao buscarNotificacaoPorOrigemEOrigemId(Notificacao.Origem origem, Long origemId) {
        return notificacaoDataProvider.buscarNotificacaoPorOrigemEOrigemId(origem.name(), origemId);
    }

    private void atualizarNotificacao(Notificacao notificacao) {
        notificacao.setVisualizada(Boolean.FALSE);
        notificacao.setMensagem("Erro no processamento");
        notificacao.setDataCriacao(LocalDateTime.now(clock));
        notificacaoDataProvider.salvar(notificacao);
    }

    private void criarNotificacao(Incorporacao incorporacao, Usuario usuario) {
        notificacaoDataProvider.salvar(
            Notificacao.builder()
                .origem(Notificacao.Origem.INCORPORACAO)
                .origemId(incorporacao.getId())
                .assunto("Incorporação " + incorporacao.getCodigo())
                .mensagem("Erro no processamento")
                .dataCriacao(LocalDateTime.now(clock))
                .visualizada(Boolean.FALSE)
                .usuario(usuario)
                .build()
        );
    }
}
