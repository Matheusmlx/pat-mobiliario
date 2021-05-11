package br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository;

import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.EmpenhoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpenhoRepository extends JpaRepository<EmpenhoEntity, Long>, QuerydslPredicateExecutor<EmpenhoEntity> {
    @Query("SELECT u FROM EmpenhoEntity u WHERE u.incorporacao.id = ?1 ORDER BY u.id DESC")
    List<EmpenhoEntity> findAllByIncorporacao(Long incorporacaoId);

    Long countByIncorporacao_Id(Long incorporacaoId);
}
