package br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.Comodante;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;

public interface ComodanteDataProvider {

    ListaPaginada<Comodante> buscarPorFiltro(Comodante.Filtro filtro);

}
