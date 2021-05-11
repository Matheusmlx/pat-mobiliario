package br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository;

import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ReservaIntervaloNumeroEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaIntervaloNumeroRepository extends JpaRepository<ReservaIntervaloNumeroEntity, Long>, QuerydslPredicateExecutor<ReservaIntervaloNumeroEntity> {

    boolean existsByReservaIntervaloId(Long reservaIntervaloId);

    boolean existsByReservaIntervaloIdIn(List<Long> reservaIntervaloIds);

    boolean existsByReservaIntervaloIdInAndUtilizadoTrue(List<Long> reservaIntervaloIds);

    @Modifying
    @Query("DELETE FROM ReservaIntervaloNumeroEntity numero where numero.reservaIntervalo.id IN ?1")
    void deleteByReservaIntervaloIdIn(List<Long> reservaIntervaloIds);

    List<ReservaIntervaloNumeroEntity> findAllByReservaIntervaloId(Long reservaIntervaloId);

    boolean existsByReservaIntervaloIdInAndUtilizadoIsTrue(List<Long> reservaIntervalosId);
}
