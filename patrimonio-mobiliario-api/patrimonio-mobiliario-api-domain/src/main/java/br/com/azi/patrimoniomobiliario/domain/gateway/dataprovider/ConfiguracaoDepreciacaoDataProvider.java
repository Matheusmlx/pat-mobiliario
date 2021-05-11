package br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.ConfiguracaoDepreciacao;

import java.util.Optional;

public interface ConfiguracaoDepreciacaoDataProvider {
    ConfiguracaoDepreciacao salvar(ConfiguracaoDepreciacao configuracaoDepreciacao);

    void excluir(Long configDepreciacaoId);

    Optional<ConfiguracaoDepreciacao> buscarPorId(Long id);
}
