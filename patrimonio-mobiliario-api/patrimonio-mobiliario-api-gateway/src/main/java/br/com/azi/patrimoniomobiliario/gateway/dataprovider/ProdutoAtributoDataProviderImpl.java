package br.com.azi.patrimoniomobiliario.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ProdutoAtributoDataProvider;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ProdutoAtributoEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository.ProdutoAtributoReadOnlyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ProdutoAtributoDataProviderImpl implements ProdutoAtributoDataProvider {

    private static final long PRODUTO_ID = 410L;

    @Autowired
    private ProdutoAtributoReadOnlyRepository produtoAtributoReadOnlyRepository;

    @Override
    public String getValor(String atributo) {
        ProdutoAtributoEntity produtoAtributo = produtoAtributoReadOnlyRepository.findByProdutoIdAndAtributo(PRODUTO_ID, atributo);

        if (Objects.nonNull(produtoAtributo))
            return produtoAtributo.getValor();

        return null;
    }
}
