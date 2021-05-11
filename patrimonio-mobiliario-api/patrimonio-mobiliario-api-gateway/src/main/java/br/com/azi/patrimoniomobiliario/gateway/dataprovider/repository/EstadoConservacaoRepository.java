package br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository;

import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.EstadoConservacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstadoConservacaoRepository extends JpaRepository<EstadoConservacaoEntity, Long> {
    List<EstadoConservacaoEntity> findAllByOrderByPrioridadeAsc();

    EstadoConservacaoEntity findFirstByOrderByPrioridadeAsc();
}
