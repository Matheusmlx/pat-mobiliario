package br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository;

import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ConfiguracaoDepreciacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfiguracaoDepreciacaoRepository extends JpaRepository<ConfiguracaoDepreciacaoEntity, Long> {
}
