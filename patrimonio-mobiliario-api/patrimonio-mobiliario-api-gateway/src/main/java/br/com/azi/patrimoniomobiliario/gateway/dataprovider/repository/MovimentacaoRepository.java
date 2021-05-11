package br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository;

import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.MovimentacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovimentacaoRepository extends JpaRepository<MovimentacaoEntity, Long>, QuerydslPredicateExecutor<MovimentacaoEntity> {

    Optional<MovimentacaoEntity> findFirstByOrderByCodigoDesc();

    Optional<MovimentacaoEntity> findFirstByNumeroTermoResponsabilidadeNotNullOrderByNumeroTermoResponsabilidadeDesc();
}
