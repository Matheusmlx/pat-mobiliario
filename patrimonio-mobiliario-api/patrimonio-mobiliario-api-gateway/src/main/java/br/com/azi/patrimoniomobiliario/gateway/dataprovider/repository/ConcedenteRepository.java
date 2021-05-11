package br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository;

import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ConcedenteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ConcedenteRepository extends JpaRepository<ConcedenteEntity,Long>, QuerydslPredicateExecutor<ConcedenteEntity> {

    Boolean existsByCpfCnpj(String cpfCnpj);

}
