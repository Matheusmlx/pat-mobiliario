package br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.ItemSimplificado;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;

public interface ItemSimplificadoDataProvider {
    ListaPaginada<ItemSimplificado> buscarPorFiltro(ItemSimplificado.Filtro filtro);
}
