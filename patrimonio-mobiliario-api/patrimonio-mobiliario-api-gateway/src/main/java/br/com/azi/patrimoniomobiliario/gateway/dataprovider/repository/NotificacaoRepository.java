package br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository;

import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.NotificacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface NotificacaoRepository extends JpaRepository<NotificacaoEntity, Long>, QuerydslPredicateExecutor<NotificacaoEntity> {
    Long countAllByVisualizadaIsFalseAndUsuarioIdEquals(Long usuarioId);

    @Modifying
    @Query("DELETE FROM NotificacaoEntity notificacao WHERE notificacao.dataCriacao < :dataReferencia")
    int removerNotificacoesPorDataCriacaoMaisAntigaQue(@Param("dataReferencia") Date dataReferencia);

    NotificacaoEntity findFirstByOrigemAndOrigemIdOrderByDataCriacaoDesc(String origem, Long origemId);
}
