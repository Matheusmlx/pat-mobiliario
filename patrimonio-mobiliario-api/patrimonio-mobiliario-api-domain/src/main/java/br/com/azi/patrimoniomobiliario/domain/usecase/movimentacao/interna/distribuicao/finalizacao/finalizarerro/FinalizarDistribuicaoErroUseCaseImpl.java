package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizarerro;

import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.Notificacao;
import br.com.azi.patrimoniomobiliario.domain.entity.Usuario;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotificacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.UsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizarerro.exception.DistribuicaoNaoEncontradaException;
import lombok.AllArgsConstructor;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
public class FinalizarDistribuicaoErroUseCaseImpl implements FinalizarDistribuicaoErroUseCase {

    private final MovimentacaoDataProvider movimentacaoDataProvider;

    private final NotificacaoDataProvider notificacaoDataProvider;

    private final UsuarioDataProvider usuarioDataProvider;

    private final Clock clock;

    @Override
    public void executar(FinalizarDistribuicaoErroInputData inputData) {
        validarDadosEntrada(inputData);

        final Movimentacao distribuicao = buscarMovimentacao(inputData.getMovimentacaoId());

        alterarDistribuicaoComInformacoesDeErroProcessamento(distribuicao, inputData);
        final Movimentacao distribuicaoAtualizada = salvarMovimentacao(distribuicao);

        notificarErroProcessamentoFinalizacaoMovimentacao(distribuicaoAtualizada);
    }

    private void validarDadosEntrada(FinalizarDistribuicaoErroInputData inputData) {
        Validator.of(inputData)
            .validate(FinalizarDistribuicaoErroInputData::getMovimentacaoId, Objects::nonNull, "Id da movimentação é nulo.")
            .get();
    }

    private Movimentacao buscarMovimentacao(Long movimentacaoId) {
        return movimentacaoDataProvider.buscarPorId(movimentacaoId)
            .orElseThrow(DistribuicaoNaoEncontradaException::new);
    }

    private void alterarDistribuicaoComInformacoesDeErroProcessamento(Movimentacao distribuicao, FinalizarDistribuicaoErroInputData inputData) {
        distribuicao.setSituacao(Movimentacao.Situacao.ERRO_PROCESSAMENTO);
        distribuicao.setDataFimProcessamento(LocalDateTime.now(clock));
        distribuicao.setErroProcessamento(inputData.getErroProcessamento());
    }

    private Movimentacao salvarMovimentacao(Movimentacao distribuicao) {
        return movimentacaoDataProvider.salvar(distribuicao);
    }

    private void notificarErroProcessamentoFinalizacaoMovimentacao(Movimentacao distribuicao) {
        final Usuario usuario = buscarUsuarioFinalizacao(distribuicao.getUsuarioFinalizacao());

        if (Objects.nonNull(usuario)) {
            final Notificacao.Origem origem = Notificacao.Origem.DISTRIBUICAO;
            final Notificacao notificacao = buscarNotificacaoPorOrigemEOrigemId(origem, distribuicao.getId());

            if (Objects.nonNull(notificacao)) {
                atualizarNotificacao(notificacao);
            } else {
                criarNotificacao(distribuicao, usuario);
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

    private void criarNotificacao(Movimentacao distribuicao, Usuario usuario) {
        notificacaoDataProvider.salvar(
            Notificacao.builder()
                .origem(Notificacao.Origem.DISTRIBUICAO)
                .origemId(distribuicao.getId())
                .assunto("Distribuição " + distribuicao.getCodigo())
                .mensagem("Erro no processamento")
                .dataCriacao(LocalDateTime.now(clock))
                .visualizada(Boolean.FALSE)
                .usuario(usuario)
                .build()
        );
    }
}
