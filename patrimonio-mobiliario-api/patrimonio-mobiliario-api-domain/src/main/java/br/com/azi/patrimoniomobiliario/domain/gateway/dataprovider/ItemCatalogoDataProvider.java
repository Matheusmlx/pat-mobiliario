package br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.ItemCatalogo;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;

import java.util.Optional;

public interface ItemCatalogoDataProvider {

    Optional<Object> buscarPorId(Long id);

    Optional<Object> buscarPorCodigo(String codigo);

    ListaPaginada<ItemCatalogo> buscarPorFiltro(ItemCatalogo.Filtro filtro);
}
