package br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository;

import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ReservaIntervaloEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaIntervaloRepository extends JpaRepository<ReservaIntervaloEntity, Long>, QuerydslPredicateExecutor<ReservaIntervaloEntity> {

    Long countAllByReservaId(Long reserva);

    Optional<ReservaIntervaloEntity> findFirstByReserva_IdOrderByNumeroFimDesc(Long reservaId);

    Optional<ReservaIntervaloEntity> findFirstByOrgao_IdAndCodigoIsNotNullOrderByCodigoDesc(Long orgaoId);

    List<ReservaIntervaloEntity> findByReservaIdOrderByOrgaoSigla(Long reservaId);

    List<ReservaIntervaloEntity> findByReservaIdAndSituacao(Long reservaId, String situacao);

    Optional<ReservaIntervaloEntity> findFirstByCodigoIsNotNullOrderByCodigoDesc();

    Optional<ReservaIntervaloEntity> findFirstByNumeroTermoIsNotNullOrderByNumeroTermoDesc();

    Boolean existsByReservaIdAndSituacao(Long reservaId, String situacao);

    @Modifying
    @Query("DELETE FROM ReservaIntervaloEntity intervalo WHERE intervalo.id IN ?1")
    void deleteByIdIn(List<Long> ids);
}
