package br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.Documento;

import java.util.List;
import java.util.Optional;

public interface DocumentoDataProvider {

    Documento salvar(Documento documento);

    boolean existeDocumentoComNumeroETipo(Long idIncorporacao, String numero, Long idTipoDocumento);

    boolean existeDocumentoComNumeroETipoEdicao(Long id, Long idIncorporacao, String numero, Long idTipoDocumento);

    boolean existeDocumentoComNumeroETipoEMovimentacao(Long idMovimentacao, String numero, Long idTipoDocumento);

    boolean existeDocumentoComNumeroETipoEMovimentacaoEdicao(Long id, Long idMovimentacao, String numero, Long idTipoDocumento);

    boolean existePorMovimentacaoId(Long movimentacaoId);

    Optional<Documento> buscarDocumentoComNumero(Long idIncorporacao, String numero);

    List<Documento> buscarDocumentoPorIncorporacaoId(Long id);

    List<Documento> buscarDocumentoPorMovimentacaoId(Long movimentacaoId);

    Optional<Documento> buscarPorId(Long id);

    void remover(Long id);

    void removeDocumentosPorIncorporacaoId(Long id);

    void removerPorMovimentacao(Long id);

    Documento atualizar(Documento documento);

    Long qntDocumentosPorIncorporacaoId(Long incorporacaoId);

    Long qntDocumentosPorMovimentacaoId(Long movimentacaoId);
}
