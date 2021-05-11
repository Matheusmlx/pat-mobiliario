package br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.Notificacao;

import java.util.Date;
import java.util.List;

public interface NotificacaoDataProvider {

    ListaPaginada<Notificacao> buscarPorFiltro(Notificacao.Filtro filtro);

    void atualizarNotificacoes(List<Notificacao> notificacoes);

    Long buscarQuantidadeNotificacoesNaoVisualizadas(Long usuarioId);

    Notificacao salvar(Notificacao notificacao);

    int removerNotificacoesAntigas(Date dataReferencia);

    Notificacao buscarNotificacaoPorOrigemEOrigemId(String origem, Long origemId);
}
