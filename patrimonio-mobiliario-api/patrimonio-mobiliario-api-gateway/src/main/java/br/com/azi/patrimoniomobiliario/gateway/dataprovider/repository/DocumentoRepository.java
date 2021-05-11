package br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository;

import br.com.azi.patrimoniomobiliario.domain.entity.Documento;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.DocumentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DocumentoRepository extends JpaRepository<DocumentoEntity, Long>, QuerydslPredicateExecutor<Documento> {

    List<DocumentoEntity> findAllByIncorporacao_Id(Long id);

    List<DocumentoEntity> findAllByMovimentacao_Id(Long movimentacaoId);

    @Query("select case when count(doc) > 0 then true else false end from DocumentoEntity doc " +
        "where doc.id<>:id and doc.incorporacao.id=:idIncorporacao and doc.numero=:numero and doc.tipoDocumento.id=:tipo")
    boolean existeNumeroTipoDocumento(
        @Param("id") Long id, @Param("idIncorporacao") Long idIncorporacao, @Param("numero") String numero,
        @Param("tipo") Long idTipoDocumento);

    @Query("select case when count(doc) > 0 then true else false end from DocumentoEntity doc " +
        "where doc.incorporacao.id=:idIncorporacao and doc.numero=:numero and doc.tipoDocumento.id=:tipo")
    boolean existeNumeroTipoDocumentoCadastro(
        @Param("idIncorporacao") Long idIncorporacao, @Param("numero") String numero,
        @Param("tipo") Long idTipoDocumento);

    @Query("select case when count(doc) > 0 then true else false end from DocumentoEntity doc " +
        "where doc.movimentacao.id = :idMovimentacao and doc.numero = :numero and doc.tipoDocumento.id = :tipo")
    boolean existeNumeroTipoDocumentoMovimentacaoCadastro(
        @Param("idMovimentacao") Long idMovimentacao, @Param("numero") String numero,
        @Param("tipo") Long idTipoDocumento);

    @Query("select case when count(doc) > 0 then true else false end from DocumentoEntity doc " +
        "where doc.id <> :id and doc.movimentacao.id = :idMovimentacao and doc.numero = :numero and doc.tipoDocumento.id = :tipo")
    boolean existeNumeroTipoDocumentoMovimentacaoEdicao(
        @Param("id") Long id, @Param("idMovimentacao") Long idMovimentacao,
        @Param("numero") String numero, @Param("tipo") Long idTipoDocumento);

    boolean existsByMovimentacao_Id(Long movimentacaoId);

    Optional<DocumentoEntity> findByIncorporacao_IdAndNumero(Long id, String numero);

    Long countByIncorporacao_Id(Long incorporacaoId);

    Long countByMovimentacao_Id(Long movimentacaoId);

    void deleteAllByMovimentacao_Id(Long id);
}
