package br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository;

import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ItemIncorporacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ItemIncorporacaoRepository extends JpaRepository<ItemIncorporacaoEntity,Long>, QuerydslPredicateExecutor<ItemIncorporacaoEntity> {

    List<ItemIncorporacaoEntity> findAllByIncorporacao_Id(Long incorporacaoId);

    Boolean existsByIncorporacao_IdAndNaturezaDespesa_IdAndCodigoLikeAndIdNot(Long incorporacaoId, Long naturezaId, String codigo, Long id);

    @Query("SELECT SUM(i.valorTotal) FROM ItemIncorporacaoEntity i WHERE i.incorporacao.id = ?1")
    BigDecimal buscaSomaDeValorTotalDosItens(Long incorporacaoId);
}
