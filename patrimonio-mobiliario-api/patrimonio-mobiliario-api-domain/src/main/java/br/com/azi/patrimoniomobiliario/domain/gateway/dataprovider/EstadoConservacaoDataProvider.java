package br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.EstadoConservacao;

import java.util.List;

public interface EstadoConservacaoDataProvider {
    List<EstadoConservacao> buscarEstadosConservacao();

    EstadoConservacao obterMelhorEstadoConservacao();
}
