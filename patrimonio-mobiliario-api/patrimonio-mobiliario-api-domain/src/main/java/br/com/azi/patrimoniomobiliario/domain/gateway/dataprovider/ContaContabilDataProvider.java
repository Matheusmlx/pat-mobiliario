package br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.ContaContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;

import java.util.Optional;

public interface ContaContabilDataProvider {

    ListaPaginada<ContaContabil> buscarPorFiltro(ContaContabil.Filtro filtro);

    Optional<ContaContabil> buscarPorId(Long id);

    Optional<ContaContabil> buscarPorCodigo(String codigo);

}
