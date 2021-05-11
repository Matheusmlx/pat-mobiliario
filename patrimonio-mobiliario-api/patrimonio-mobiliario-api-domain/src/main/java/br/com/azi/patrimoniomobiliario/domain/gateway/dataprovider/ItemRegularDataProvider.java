package br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.ItemRegular;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;

public interface ItemRegularDataProvider {
    ListaPaginada<ItemRegular> buscarPorFiltro(ItemRegular.Filtro filtro);
}
