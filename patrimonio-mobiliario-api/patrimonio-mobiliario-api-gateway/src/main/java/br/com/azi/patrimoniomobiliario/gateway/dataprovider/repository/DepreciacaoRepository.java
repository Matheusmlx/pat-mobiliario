package br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository;

import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.DepreciacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface DepreciacaoRepository  extends JpaRepository<DepreciacaoEntity, Long>, QuerydslPredicateExecutor<DepreciacaoEntity> {

    List<DepreciacaoEntity> findByPatrimonio_IdOrderByDataFinal(Long patrimonioId);

    Boolean existsByPatrimonio_Id(Long patrimonioId);

    Boolean existsByPatrimonio_IdAndDataInicial(Long patrimonioId, Date mesReferencia);

    Optional<DepreciacaoEntity> findFirstByPatrimonio_IdOrderByDataFinalDesc(Long patrimonioId);

}
