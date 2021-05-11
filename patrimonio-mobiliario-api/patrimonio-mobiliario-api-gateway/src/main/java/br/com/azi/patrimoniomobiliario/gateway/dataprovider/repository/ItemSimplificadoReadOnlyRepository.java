package br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository;

import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ItemSimplificadoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemSimplificadoReadOnlyRepository extends JpaRepository<ItemSimplificadoEntity,Long>, QuerydslPredicateExecutor<ItemSimplificadoEntity> {

    Optional<ItemSimplificadoEntity> findByCodigo(String codigo);

    @Query(value = "SELECT ITEM FROM ItemSimplificadoEntity ITEM " +
        "INNER JOIN MaterialServicoEntity MS ON ITEM.materialServicoId = MS.id " +
        "INNER JOIN VinculoMaterialServicoSubelementoEntity MSS ON MS.id = MSS.vinculoMaterialServicoSubElementoId.materialServico.id " +
        "INNER JOIN ContratacaoSubelementoEntity CS ON CS.id = MSS.vinculoMaterialServicoSubElementoId.contratacaoSubelemento.id " +
        "INNER JOIN NaturezaContratacaoEntity NC ON CS.naturezaContratacao.id = NC.id " +
        "WHERE NC.tipo ='PERMANENTE' " +
        "AND ((ITEM.codigo IS NOT NULL) AND (ITEM.situacao = 'ATIVO'))")
    Page<ItemSimplificadoEntity> findAllByNaturezaContratacaoPermanente(Pageable pagination);

    @Query(value = "SELECT ITEM FROM ItemSimplificadoEntity ITEM " +
        "INNER JOIN MaterialServicoEntity MS ON ITEM.materialServicoId = MS.id " +
        "INNER JOIN VinculoMaterialServicoSubelementoEntity MSS ON MS.id = MSS.vinculoMaterialServicoSubElementoId.materialServico.id " +
        "INNER JOIN ContratacaoSubelementoEntity CS ON CS.id = MSS.vinculoMaterialServicoSubElementoId.contratacaoSubelemento.id " +
        "INNER JOIN NaturezaContratacaoEntity NC ON CS.naturezaContratacao.id = NC.id " +
        "WHERE NC.tipo ='PERMANENTE' " +
        "AND (((ITEM.codigo IS NOT NULL) AND (ITEM.situacao = 'ATIVO'))" +
        "AND ((TRIM(UPPER(ITEM.codigo)) LIKE UPPER(:content)) OR " +
        "(((TRIM(UPPER(TRANSLATE(ITEM.descricao, 'âàãáÁÂÀÃéêÉÊíÍóôõÓÔÕüúÜÚÇç', 'AAAAAAAAEEEEIIOOOOOOUUUUCC'))) LIKE UPPER(:content))))))")
    Page<ItemSimplificadoEntity> findAllByNaturezaContratacaoPermanenteFiltro(@Param(value = "content") String expression, Pageable pagination);
}
