package br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository;


import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ConfigContaContabilEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfigContaContabilRepository extends JpaRepository<ConfigContaContabilEntity, Long> {

    Optional<ConfigContaContabilEntity> findFirstByContaContabil_idOrderByDataCadastroDesc(Long contaContabilId);

    Boolean existsByContaContabil_Id(Long contaContabilId);
}
