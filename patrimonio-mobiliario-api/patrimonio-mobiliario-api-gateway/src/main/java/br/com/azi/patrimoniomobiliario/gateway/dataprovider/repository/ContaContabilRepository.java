package br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository;

import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ContaContabilEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContaContabilRepository extends JpaRepository<ContaContabilEntity,Long>, QuerydslPredicateExecutor<ContaContabilEntity> {
    Optional<ContaContabilEntity> findByCodigo(String codigo);
}
