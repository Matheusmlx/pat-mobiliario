package br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.ConfigContaContabil;

import java.util.Optional;

public interface ConfigContaContabilDataProvider {
    ConfigContaContabil salvar(ConfigContaContabil configContaContabil);

    ConfigContaContabil atualizar(ConfigContaContabil configContaContabil);

    Optional<ConfigContaContabil> buscarPorId(Long id);

    Optional<ConfigContaContabil> buscarAtualPorContaContabil(Long contaContabilId);

    boolean existe(Long id);

    boolean existePorContaContabil(Long contaContabilId);
}
