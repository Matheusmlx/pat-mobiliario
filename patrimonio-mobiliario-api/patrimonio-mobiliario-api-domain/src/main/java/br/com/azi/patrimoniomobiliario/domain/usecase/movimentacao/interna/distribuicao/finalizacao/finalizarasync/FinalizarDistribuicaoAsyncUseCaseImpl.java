package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizarasync;

import br.com.azi.patrimoniomobiliario.domain.entity.ContaContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.MovimentacaoItem;
import br.com.azi.patrimoniomobiliario.domain.entity.Notificacao;
import br.com.azi.patrimoniomobiliario.domain.entity.Usuario;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ContaContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotificacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.UsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizarasync.exception.ContaContabilAlmoxarifadoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizarasync.exception.DistribuicaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.helpers.DistribuirPatrimonioHelper;
import lombok.AllArgsConstructor;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
public class FinalizarDistribuicaoAsyncUseCaseImpl implements FinalizarDistribuicaoAsyncUseCase {

    private final Clock clock;

    private final MovimentacaoDataProvider movimentacaoDataProvider;

    private final MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    private final ContaContabilDataProvider contaContabilDataProvider;

    private final NotificacaoDataProvider notificacaoDataProvider;

    private final UsuarioDataProvider usuarioDataProvider;

    private final DistribuirPatrimonioHelper distribuirPatrimonioHelper;

    private final boolean patrimonioParaContaAlmoxarifado;

    private final String codigoContaContabilAlmoxarifado;

    @Override
    public void executar(FinalizarDistribuicaoAsyncInputData inputData) {
        final Movimentacao distribuicao = buscarMovimentacao(inputData);

        ContaContabil contaContabilAlmoxarifado = null;
        if (patrimonioParaContaAlmoxarifado) {
            contaContabilAlmoxarifado = buscarContaContabilAlmoxarifado();
        }

        final MovimentacaoItemChunkReader chunkReader = new MovimentacaoItemChunkReader(
            movimentacaoItemDataProvider,
            distribuicao.getId()
        );

        while (chunkReader.hasNext()) {
            final List<MovimentacaoItem> movimentacaoItens = chunkReader.next();
            processarItensDistribuicao(movimentacaoItens, distribuicao, contaContabilAlmoxarifado);
        }

        atualizarDistribuicaoComInformacoesProcessamentoSucesso(distribuicao);
        final Movimentacao distribuicaoFinalizada = salvarMovimentacao(distribuicao);

        notificarDistribuicaoFinalizada(distribuicaoFinalizada);
    }

    private Movimentacao buscarMovimentacao(FinalizarDistribuicaoAsyncInputData inputData) {
        return movimentacaoDataProvider.buscarPorId(inputData.getMovimentacaoId())
            .orElseThrow(() -> new DistribuicaoNaoEncontradaException(inputData.getMovimentacaoId()));
    }

    private ContaContabil buscarContaContabilAlmoxarifado() {
        return contaContabilDataProvider.buscarPorCodigo(codigoContaContabilAlmoxarifado)
            .orElseThrow(ContaContabilAlmoxarifadoNaoEncontradaException::new);
    }

    private void processarItensDistribuicao(List<MovimentacaoItem> movimentacaoItens,
                                            Movimentacao distribuicao, ContaContabil contaAlmoxarifado) {
        for (MovimentacaoItem movimentacaoItem : movimentacaoItens) {
            distribuirPatrimonioHelper.distribuirPatrimonio(movimentacaoItem, distribuicao, contaAlmoxarifado);
        }
    }

    private void atualizarDistribuicaoComInformacoesProcessamentoSucesso(Movimentacao distribuicao) {
        distribuicao.setSituacao(Movimentacao.Situacao.FINALIZADO);
        distribuicao.setDataFimProcessamento(LocalDateTime.now(clock));
        distribuicao.setDataFinalizacao(LocalDateTime.now(clock));
    }

    private Movimentacao salvarMovimentacao(Movimentacao distribuicao) {
        return movimentacaoDataProvider.salvar(distribuicao);
    }

    private void notificarDistribuicaoFinalizada(Movimentacao distribuicao) {
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
        notificacao.setMensagem("Finalizado");
        notificacao.setDataCriacao(LocalDateTime.now(clock));
        notificacaoDataProvider.salvar(notificacao);
    }

    private void criarNotificacao(Movimentacao distribuicao, Usuario usuario) {
        notificacaoDataProvider.salvar(
            Notificacao.builder()
                .origem(Notificacao.Origem.DISTRIBUICAO)
                .origemId(distribuicao.getId())
                .assunto("Distribuição " + distribuicao.getCodigo())
                .mensagem("Finalizado")
                .dataCriacao(LocalDateTime.now(clock))
                .visualizada(Boolean.FALSE)
                .usuario(usuario)
                .build());
    }

}
