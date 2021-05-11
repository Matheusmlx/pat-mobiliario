package br.com.azi.patrimoniomobiliario.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.ConfiguracaoDepreciacao;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ConfiguracaoDepreciacaoDataProvider;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter.ConfiguracaoDepreciacaoConverter;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ConfiguracaoDepreciacaoEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository.ConfiguracaoDepreciacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class ConfiguracaoDepreciacaoDataProviderImpl implements ConfiguracaoDepreciacaoDataProvider {

    @Autowired
    private ConfiguracaoDepreciacaoRepository repository;

    @Autowired
    private ConfiguracaoDepreciacaoConverter converter;

    @Override
    @Modifying(flushAutomatically = true)
    @Transactional
    public ConfiguracaoDepreciacao salvar(ConfiguracaoDepreciacao configuracaoDepreciacao) {
        ConfiguracaoDepreciacaoEntity configuracaoDepreciacaoSalva = repository.save(converter.from(configuracaoDepreciacao));
        return converter.to(configuracaoDepreciacaoSalva);
    }

    @Override
    @Modifying(flushAutomatically = true)
    @Transactional
    public void excluir(Long configDepreciacaoId) {
        repository.deleteById(configDepreciacaoId);
    }

    @Override
    public Optional<ConfiguracaoDepreciacao> buscarPorId(Long id) {
        Optional<ConfiguracaoDepreciacaoEntity> configuracaoDepreciacao = repository.findById(id);
        return configuracaoDepreciacao.map(configuracaoDepreciacaoEntity -> converter.to(configuracaoDepreciacaoEntity));
    }
}
