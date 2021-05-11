package br.com.azi.patrimoniomobiliario.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.Documento;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter.DocumentoConverter;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.DocumentoEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository.DocumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class DocumentoDataProviderImpl implements DocumentoDataProvider {

    @Autowired
    private DocumentoRepository repository;

    @Autowired
    private DocumentoConverter converter;

    @Override
    public Optional<Documento> buscarPorId(Long id) {
        Optional<DocumentoEntity> encontrada = repository.findById(id);
        return encontrada.map(documentoEntity -> converter.to(documentoEntity));
    }

    @Override
    public List<Documento> buscarDocumentoPorIncorporacaoId(Long idIncorporacao) {
        List<DocumentoEntity> documentos = repository.findAllByIncorporacao_Id(idIncorporacao);

        return documentos
            .stream()
            .map(converter::to)
            .collect(Collectors.toList());
    }

    @Override
    public List<Documento> buscarDocumentoPorMovimentacaoId(Long movimentacaoId) {
        List<DocumentoEntity> documentos = repository.findAllByMovimentacao_Id(movimentacaoId);

        return documentos
            .stream()
            .map(converter::to)
            .collect(Collectors.toList());
    }

    @Override
    public Documento salvar(Documento business) {
        DocumentoEntity salva = repository.save(converter.from(business));
        return converter.to(salva);
    }

    @Override
    @Transactional
    public Documento atualizar(Documento business) {
        DocumentoEntity salvo = repository.save(converter.from(business));
        return converter.to(salvo);
    }

    @Override
    public boolean existeDocumentoComNumeroETipo(Long idIncorporacao, String numero, Long idTipoDocumento) {
        return repository.existeNumeroTipoDocumentoCadastro(idIncorporacao, numero, idTipoDocumento);
    }

    public boolean existeDocumentoComNumeroETipoEdicao(Long id, Long idIncorporacao, String numero, Long idTipoDocumento) {
        return repository.existeNumeroTipoDocumento(id, idIncorporacao, numero, idTipoDocumento);
    }

    @Override
    public boolean existeDocumentoComNumeroETipoEMovimentacao(Long idMovimentacao, String numero, Long idTipoDocumento) {
        return repository.existeNumeroTipoDocumentoMovimentacaoCadastro(idMovimentacao, numero, idTipoDocumento);
    }

    @Override
    public boolean existeDocumentoComNumeroETipoEMovimentacaoEdicao(Long id, Long idMovimentacao, String numero, Long idTipoDocumento) {
        return repository.existeNumeroTipoDocumentoMovimentacaoEdicao(id, idMovimentacao, numero, idTipoDocumento);
    }

    @Override
    public boolean existePorMovimentacaoId(Long movimentacaoId) {
        return repository.existsByMovimentacao_Id(movimentacaoId);
    }

    @Override
    public Optional<Documento> buscarDocumentoComNumero(Long idIncorporacao, String numero) {
        Optional<DocumentoEntity> encontrada = repository.findByIncorporacao_IdAndNumero(idIncorporacao, numero);
        return encontrada.map(documentoEntity -> converter.to(documentoEntity));
    }

    @Override
    @Transactional
    public void remover(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public void removeDocumentosPorIncorporacaoId(Long idIncorporacao) {
        List<Documento> documentos = buscarDocumentoPorIncorporacaoId(idIncorporacao);
        for (Documento documento : documentos) {
            remover(documento.getId());
        }
    }

    @Override
    @Transactional
    @Modifying(flushAutomatically = true)
    public void removerPorMovimentacao(Long id) {
        repository.deleteAllByMovimentacao_Id(id);
    }

    @Override
    public Long qntDocumentosPorIncorporacaoId(Long incorporacaoId){
        return repository.countByIncorporacao_Id(incorporacaoId);
    }

    @Override
    public Long qntDocumentosPorMovimentacaoId(Long movimentacaoId) {
        return repository.countByMovimentacao_Id(movimentacaoId);
    }
}
