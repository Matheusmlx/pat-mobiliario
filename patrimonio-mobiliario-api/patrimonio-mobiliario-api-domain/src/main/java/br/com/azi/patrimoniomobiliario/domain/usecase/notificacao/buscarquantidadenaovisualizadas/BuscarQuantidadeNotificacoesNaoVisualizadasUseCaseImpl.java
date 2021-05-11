package br.com.azi.patrimoniomobiliario.domain.usecase.notificacao.buscarquantidadenaovisualizadas;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotificacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.notificacao.buscarquantidadenaovisualizadas.converter.BuscarQuantidadeNotificacoesNaoVisualizadasOutputDataConverter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BuscarQuantidadeNotificacoesNaoVisualizadasUseCaseImpl implements BuscarQuantidadeNotificacoesNaoVisualizadasUseCase {

    private final NotificacaoDataProvider notificacaoDataProvider;

    private final SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    private final BuscarQuantidadeNotificacoesNaoVisualizadasOutputDataConverter outputDataConverter;

    @Override
    public BuscarQuantidadeNotificacoesNaoVisualizadasOutputData executar() {
        final Long quantidadeNotificacoesNaoVisualizadas = buscarQuantidadeNotificacoesNaoVisualizadas();

        return outputDataConverter.to(quantidadeNotificacoesNaoVisualizadas);
    }

    private Long buscarQuantidadeNotificacoesNaoVisualizadas() {
        return notificacaoDataProvider.buscarQuantidadeNotificacoesNaoVisualizadas(sessaoUsuarioDataProvider.get().getId());
    }
}
