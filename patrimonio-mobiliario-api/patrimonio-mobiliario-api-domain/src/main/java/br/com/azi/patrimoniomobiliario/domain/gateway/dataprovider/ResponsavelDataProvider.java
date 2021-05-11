package br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.Responsavel;

import java.util.List;

public interface ResponsavelDataProvider {

    List<Responsavel> buscarResponsaveis();

}
