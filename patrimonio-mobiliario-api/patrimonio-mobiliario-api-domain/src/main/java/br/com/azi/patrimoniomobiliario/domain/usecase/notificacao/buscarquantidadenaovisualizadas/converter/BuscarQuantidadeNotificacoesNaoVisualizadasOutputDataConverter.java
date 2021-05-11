package br.com.azi.patrimoniomobiliario.domain.usecase.notificacao.buscarquantidadenaovisualizadas.converter;

import br.com.azi.patrimoniomobiliario.domain.usecase.notificacao.buscarquantidadenaovisualizadas.BuscarQuantidadeNotificacoesNaoVisualizadasOutputData;

public class BuscarQuantidadeNotificacoesNaoVisualizadasOutputDataConverter {

    public BuscarQuantidadeNotificacoesNaoVisualizadasOutputData to(Long quantidadeNaoVisualizadas) {
        return BuscarQuantidadeNotificacoesNaoVisualizadasOutputData
            .builder()
            .quantidadeNaoVisualizadas(quantidadeNaoVisualizadas)
            .build();
    }

}
