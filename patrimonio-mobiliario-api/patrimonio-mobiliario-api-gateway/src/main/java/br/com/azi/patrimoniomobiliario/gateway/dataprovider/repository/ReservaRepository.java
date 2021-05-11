package br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository;

import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ReservaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReservaRepository extends JpaRepository<ReservaEntity, Long>, QuerydslPredicateExecutor<ReservaEntity> {

    Optional<ReservaEntity> findFirstByNumeroFimIsNotNullOrderByNumeroFimDesc();

    Optional<ReservaEntity> findFirstByCodigoIsNotNullOrderByCodigoDesc();

}
