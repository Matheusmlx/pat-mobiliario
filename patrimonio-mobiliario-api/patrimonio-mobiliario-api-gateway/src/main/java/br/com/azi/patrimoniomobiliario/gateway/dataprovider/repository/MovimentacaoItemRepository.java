package br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository;

import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.MovimentacaoItemEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.MovimentacaoItemPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovimentacaoItemRepository extends JpaRepository<MovimentacaoItemEntity, MovimentacaoItemPK>, QuerydslPredicateExecutor<MovimentacaoItemEntity> {

    List<MovimentacaoItemEntity> findAllByMovimentacaoItemPK_Patrimonio_IdOrderByMovimentacaoItemPK_Movimentacao_DataFinalizacaoDesc(Long id);

    Optional<MovimentacaoItemEntity> findByMovimentacaoItemPK_Movimentacao_IdAndMovimentacaoItemPK_Patrimonio_Id(Long movimentacaoId, Long patrimonioId);

    void deleteAllByMovimentacaoItemPK_Movimentacao_Id(Long id);

    List<MovimentacaoItemEntity> findAllByMovimentacaoItemPK_Movimentacao_Id(Long movimentacaoId);

    Long countByMovimentacaoItemPK_Movimentacao_Id(Long movimentacaoId);

    boolean existsByMovimentacaoItemPK_Movimentacao_Id(Long movimentacaoId);

    boolean existsByMovimentacaoItemPK_Movimentacao_IdAndMovimentacaoItemPK_Patrimonio_IdAndDataDevolucaoIsNull(Long movimentacao, Long patrimonio);

    List<MovimentacaoItemEntity> findAllByMovimentacaoItemPK_Movimentacao_IdAndDataDevolucaoIsNull(Long movimentacaoId);

    Long countByMovimentacaoItemPK_Movimentacao_IdAndDataDevolucaoIsNotNull(Long movimentacaoId);

    boolean existsByMovimentacaoItemPK_Movimentacao_IdAndDataDevolucaoIsNull(Long movimentacaoId);

    boolean existsByMovimentacaoItemPK_Patrimonio_IdAndMovimentacaoItemPK_MovimentacaoTipo(Long patrimonioId, String tipo);

    @Query("SELECT CASE WHEN COUNT(movimentacaoItem) > 0 THEN true ELSE false END FROM MovimentacaoItemEntity movimentacaoItem " +
        "WHERE movimentacaoItem.movimentacaoItemPK.patrimonio.id = :patrimonioId " +
        "AND movimentacaoItem.movimentacaoItemPK.movimentacao.tipo = 'DISTRIBUICAO' " +
        "AND movimentacaoItem.movimentacaoItemPK.movimentacao.situacao = 'FINALIZADO'")
    boolean existeDistribuicaoFinalizadaParaPatrimonio(@Param("patrimonioId") Long patrimonioId);

}
