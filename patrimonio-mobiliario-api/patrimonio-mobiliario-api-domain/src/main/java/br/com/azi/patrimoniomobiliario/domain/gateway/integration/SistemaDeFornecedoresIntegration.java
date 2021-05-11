package br.com.azi.patrimoniomobiliario.domain.gateway.integration;


import br.com.azi.patrimoniomobiliario.domain.entity.Fornecedor;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;

public interface SistemaDeFornecedoresIntegration {

    ListaPaginada<Fornecedor> buscarPorFiltro(Fornecedor.Filtro filtro);

    Fornecedor buscarPorId(Long id);

}
