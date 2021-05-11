package br.com.azi.patrimoniomobiliario.domain.usecase.notificacao.listagem;

import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.Notificacao;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotificacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.notificacao.listagem.converter.BuscarNotificacoesUsuarioFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.notificacao.listagem.converter.BuscarNotificacoesUsuarioOutputDataConverter;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
public class BuscarNotificacoesUsuarioUseCaseImpl implements BuscarNotificacoesUsuarioUseCase {

    private final NotificacaoDataProvider notificacaoDataProvider;

    private final BuscarNotificacoesUsuarioFiltroConverter filtroConverter;

    private final BuscarNotificacoesUsuarioOutputDataConverter outputDataConverter;

    private final SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    @Override
    public BuscarNotificacoesUsuarioOutputData executar(BuscarNotificacoesUsuarioInputData inputData) {
        validarDadosEntrada(inputData);

        final Notificacao.Filtro filtro = criarFiltro(inputData);
        final ListaPaginada<Notificacao> notificacoes = buscarNotificacoesUsuario(filtro);
        final Long quantidadeNotificacoesNaoVisualizadas = buscarQuantidadeNotificacoesNaoVisualizadas();
        List<Notificacao> notificacoesNaoVisualizadas = obterNotificacoesNaoAtualizadas(notificacoes.getItems());

        final BuscarNotificacoesUsuarioOutputData outputData = outputDataConverter.to(notificacoes, quantidadeNotificacoesNaoVisualizadas - notificacoesNaoVisualizadas.size());
        atualizarNotificacoesNaoVisualizadas(notificacoesNaoVisualizadas);

        return outputData;
    }

    private void validarDadosEntrada(BuscarNotificacoesUsuarioInputData inputData) {
        Validator.of(inputData)
                .validate(BuscarNotificacoesUsuarioInputData::getPage, page -> Objects.nonNull(page) && page > 0, "Número da página nulo")
                .get();
    }

    private Notificacao.Filtro criarFiltro(BuscarNotificacoesUsuarioInputData inputData) {
        return filtroConverter.to(inputData, sessaoUsuarioDataProvider.get().getId());
    }

    private ListaPaginada<Notificacao> buscarNotificacoesUsuario(Notificacao.Filtro filtro) {
        return notificacaoDataProvider.buscarPorFiltro(filtro);
    }

    private List<Notificacao> obterNotificacoesNaoAtualizadas(List<Notificacao> notificacoes) {
        return notificacoes
                .stream()
                .filter(BuscarNotificacoesUsuarioUseCaseImpl::verificarNotificacaoNaoVisualizada)
                .collect(Collectors.toList());
    }

    private void atualizarNotificacoesNaoVisualizadas(List<Notificacao> notificacoes) {
        notificacoes.forEach(BuscarNotificacoesUsuarioUseCaseImpl::atualizarNotificacao);

        notificacaoDataProvider.atualizarNotificacoes(notificacoes);
    }

    private static boolean verificarNotificacaoNaoVisualizada(Notificacao notificacao) {
        return Boolean.FALSE.equals(notificacao.getVisualizada());
    }

    private static void atualizarNotificacao(Notificacao notificacao) {
        notificacao.setVisualizada(true);
    }

    private Long buscarQuantidadeNotificacoesNaoVisualizadas() {
        return notificacaoDataProvider.buscarQuantidadeNotificacoesNaoVisualizadas(sessaoUsuarioDataProvider.get().getId());
    }
}