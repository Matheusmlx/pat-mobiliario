package br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository;

import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ItemIncorporacaoEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.PatrimonioEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatrimonioRepository extends JpaRepository<PatrimonioEntity, Long>, QuerydslPredicateExecutor<PatrimonioEntity> {
    Boolean existsByChassiAndIdNot(String chassi, Long id);

    Boolean existsByNumeroSerieAndIdNot(String numeroSerie, Long id);

    Boolean existsByMotorAndIdNot(String motor, Long id);

    Boolean existsByPlacaAndIdNot(String placa, Long id);

    Boolean existsByRenavamAndIdNot(String renavam, Long id);

    Page<PatrimonioEntity> findAllByItemIncorporacaoEquals(ItemIncorporacaoEntity itemIncorporacao, Pageable pageable);

    Page<PatrimonioEntity> findAllByItemIncorporacaoEqualsAndSituacaoEquals(ItemIncorporacaoEntity itemIncorporacao, Pageable pageable, String situacao);

    List<PatrimonioEntity> findAllByItemIncorporacao_Id(Long itemIncorporacaoId);

    List<PatrimonioEntity> findAllByItemIncorporacao_Incorporacao_Id(Long incorporacaoId);

    List<PatrimonioEntity> findAllByIdIn(List<Long> patrimonioId);

    @Query("SELECT pat FROM PatrimonioEntity pat " +
        "JOIN ItemIncorporacaoEntity item ON (pat.itemIncorporacao.id = item.id) " +
        "JOIN IncorporacaoEntity inc ON (item.incorporacao.id = inc.id)" +
        "WHERE inc.id = ?1 AND pat.situacao = 'ATIVO' " +
        "AND pat.id NOT IN ?2")
    List<PatrimonioEntity> buscarPatrimoniosAtivosPorIncorporacaoId(Long incorporacaoId, List<Long> patrimoniosExcecao);

    @Query("SELECT COUNT(pat) FROM PatrimonioEntity pat " +
        "WHERE NOT EXISTS (" +
        "  SELECT 1 FROM MovimentacaoItemEntity mov_item WHERE " +
        "  mov_item.movimentacaoItemPK.movimentacao.situacao = 'EM_ELABORACAO' AND " +
        "  mov_item.movimentacaoItemPK.patrimonio = pat" +
        ") AND " +
        "pat.situacao = 'ATIVO' AND " +
        "pat.orgao.id = :orgao AND " +
        "pat.setor.id = :setor ")
    Long buscarQuantidadePatrimoniosQueNaoEstaoEmOutraMovimentacaoEmElaboracao(@Param("orgao") Long orgao, @Param("setor") Long setor);

    @Query("SELECT COUNT(pat) FROM PatrimonioEntity pat " +
        "JOIN ItemIncorporacaoEntity item ON (pat.itemIncorporacao.id = item.id) " +
        "JOIN IncorporacaoEntity inc ON (item.incorporacao.id = inc.id)" +
        "WHERE inc.id = ?1")
    Long buscarQuantidadePatrimoniosPorIncorporacaoId(Long incorporacaoId);

    @Query("SELECT COUNT(pat) FROM PatrimonioEntity pat " +
        "JOIN ItemIncorporacaoEntity item ON (pat.itemIncorporacao.id = item.id) " +
        "JOIN IncorporacaoEntity inc ON (item.incorporacao.id = inc.id)" +
        "WHERE inc.id = ?1 AND pat.situacao = 'ATIVO'")
    Long buscarQuantidadePatrimoniosAtivosPorIncorporacaoId(Long incorporacaoId);

    Optional<PatrimonioEntity> findFirstByNumeroNotNullOrderByNumeroDesc();

    Long countAllByItemIncorporacao_Id(Long itemIncorporacaoId);

    Optional<PatrimonioEntity> findFirstByItemIncorporacao_IdOrderByNumeroDesc(Long itemIncorporacaoId);

    void deleteById(Long patrimonioId);

    @Query("SELECT COUNT(pat) FROM PatrimonioEntity pat " +
        "JOIN ItemIncorporacaoEntity itemIncorporacao ON (pat.itemIncorporacao.id = itemIncorporacao.id) " +
        "JOIN IncorporacaoEntity inc ON (itemIncorporacao.incorporacao.id = inc.id) " +
        "JOIN MovimentacaoItemEntity movimentacaoItem ON (pat.id = movimentacaoItem.movimentacaoItemPK.patrimonio.id) " +
        "JOIN MovimentacaoEntity movimentacao ON (movimentacaoItem.movimentacaoItemPK.movimentacao.id = movimentacao.id) " +
        "WHERE inc.id = ?1 AND movimentacao.tipo <> 'INCORPORACAO'")
    Long buscarQuantidadePatrimoniosEmOutrasMovimentacoesPorIncorporacaoId(Long incorporacaoId);

    @Query("SELECT pat FROM PatrimonioEntity pat WHERE pat.itemIncorporacao.incorporacao.id = :incorporacaoId")
    Page<PatrimonioEntity> buscarPorIncorporacaoPaginado(@Param("incorporacaoId") Long incorporacaoId, Pageable pageable);

    @Query("SELECT patrimonio FROM PatrimonioEntity patrimonio WHERE patrimonio.situacao = 'ATIVO' AND patrimonio.depreciavel = true")
    Page<PatrimonioEntity> buscarPatrimoniosDepreciaveis(Pageable pageable);

    Optional<PatrimonioEntity> findFirstByNumeroNotNullAndOrgao_IdOrderByNumeroDesc(Long orgaoId);
}
