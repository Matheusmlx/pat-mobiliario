package br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository;

import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ProdutoAtributoEntity;
import org.springframework.data.repository.Repository;

public interface ProdutoAtributoReadOnlyRepository extends Repository<ProdutoAtributoEntity, Long> {
    ProdutoAtributoEntity findByProdutoIdAndAtributo(Long produtoId, String atributo);
}
