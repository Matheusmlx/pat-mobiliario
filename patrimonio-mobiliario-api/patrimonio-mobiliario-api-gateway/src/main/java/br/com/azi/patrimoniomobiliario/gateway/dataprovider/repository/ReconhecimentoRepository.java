package br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository;

import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ReconhecimentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReconhecimentoRepository extends JpaRepository<ReconhecimentoEntity, Long>, QuerydslPredicateExecutor<ReconhecimentoEntity> {

    Boolean existsByNome(String nome);

    Optional<ReconhecimentoEntity>findByNomeAndId(String nome , Long id);
}
