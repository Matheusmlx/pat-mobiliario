package br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository;

import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.IncorporacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IncorporacaoRepository extends JpaRepository<IncorporacaoEntity,Long>, QuerydslPredicateExecutor<IncorporacaoEntity> {
}
