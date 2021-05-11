package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizarasync;

import br.com.azi.patrimoniomobiliario.domain.entity.ContaContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.ItemIncorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.Notificacao;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.entity.Usuario;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ContaContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ItemIncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotificacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.UsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizarasync.exception.ContaContabilAlmoxarifadoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizarasync.exception.IncorporacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.helpers.IncorporarPatrimonioHelper;
import lombok.AllArgsConstructor;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
public class FinalizarIncorporacaoAsyncUseCaseImpl implements FinalizarIncorporacaoAsyncUseCase {

    private final Clock clock;

    private final PatrimonioDataProvider patrimonioDataProvider;

    private final IncorporacaoDataProvider incorporacaoDataProvider;

    private final ItemIncorporacaoDataProvider itemIncorporacaoDataProvider;

    private final ContaContabilDataProvider contaContabilDataProvider;

    private final NotificacaoDataProvider notificacaoDataProvider;

    private final UsuarioDataProvider usuarioDataProvider;

    private final IncorporarPatrimonioHelper incorporarPatrimonioHelper;

    private final Boolean patrimonioParaContaAlmoxarifado;

    private final String codigoContaContabilAlmoxarifado;

    @Override
    public void executar(FinalizarIncorporacaoAsyncInputData inputData) {
        final Incorporacao incorporacao = buscarIncorporacao(inputData.getIncorporacaoId());
        final PatrimonioIncorporacaoChunkReader chunkReader = new PatrimonioIncorporacaoChunkReader(patrimonioDataProvider, incorporacao.getId());
        final Map<Long, ItemIncorporacao> itensIncorporacaoMap = buscarItensIncorporacao(incorporacao.getId());

        ContaContabil contaAlmoxarifado = null;
        if (Boolean.TRUE.equals(patrimonioParaContaAlmoxarifado)) {
            contaAlmoxarifado = buscaContaContabilAlmoxarifado();
        }

        while (chunkReader.hasNext()) {
            final List<Patrimonio> patrimonios = chunkReader.next();
            processarFinalizacaoPatrimonios(patrimonios, incorporacao, itensIncorporacaoMap, contaAlmoxarifado);
        }

        atualizarIncorporacaoComInformacoesProcessamentoSucesso(incorporacao);

        final Incorporacao incorporacaoFinalizada = salvarIncorporacao(incorporacao);

        notificarIncorporacaoFinalizada(incorporacaoFinalizada);
    }

    private Incorporacao buscarIncorporacao(Long incorporacaoId) {
        return incorporacaoDataProvider.buscarPorId(incorporacaoId)
            .orElseThrow(() -> new IncorporacaoNaoEncontradaException(incorporacaoId));
    }

    private Map<Long, ItemIncorporacao> buscarItensIncorporacao(Long incorporacaoId) {
        final List<ItemIncorporacao> itens = itemIncorporacaoDataProvider.buscarItensPorIncorporacaoId(incorporacaoId);
        return itens.stream().collect(Collectors.toMap(ItemIncorporacao::getId, itemIncorporacao -> itemIncorporacao));
    }

    private ContaContabil buscaContaContabilAlmoxarifado() {
        return contaContabilDataProvider.buscarPorCodigo(codigoContaContabilAlmoxarifado)
            .orElseThrow(ContaContabilAlmoxarifadoNaoEncontradaException::new);
    }

    private void processarFinalizacaoPatrimonios(List<Patrimonio> patrimonios, Incorporacao incorporacao, Map<Long, ItemIncorporacao> itensIncorporacaoMap, ContaContabil contaAlmoxarifado) {
        for (Patrimonio patrimonio : patrimonios) {
            final ItemIncorporacao itemIncorporacao = itensIncorporacaoMap.get(patrimonio.getItemIncorporacao().getId());
            incorporarPatrimonioHelper.incorporarPatrimonio(patrimonio, itemIncorporacao, incorporacao, contaAlmoxarifado);
        }
    }

    private void atualizarIncorporacaoComInformacoesProcessamentoSucesso(Incorporacao incorporacao) {
        incorporacao.setSituacao(Incorporacao.Situacao.FINALIZADO);
        incorporacao.setDataFimProcessamento(LocalDateTime.now(clock));
    }

    private Incorporacao salvarIncorporacao(Incorporacao incorporacao) {
        return incorporacaoDataProvider.salvar(incorporacao);
    }

    private void notificarIncorporacaoFinalizada(Incorporacao incorporacao) {
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
        notificacao.setMensagem("Finalizado");
        notificacao.setDataCriacao(LocalDateTime.now(clock));
        notificacaoDataProvider.salvar(notificacao);
    }

    private void criarNotificacao(Incorporacao incorporacao, Usuario usuario) {
        notificacaoDataProvider.salvar(
            Notificacao.builder()
                .origem(Notificacao.Origem.INCORPORACAO)
                .origemId(incorporacao.getId())
                .assunto("Incorporação " + incorporacao.getCodigo())
                .mensagem("Finalizado")
                .dataCriacao(LocalDateTime.now(clock))
                .visualizada(Boolean.FALSE)
                .usuario(usuario)
                .build());
    }


}
