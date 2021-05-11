package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.remover;

import br.com.azi.patrimoniomobiliario.domain.entity.Arquivo;
import br.com.azi.patrimoniomobiliario.domain.entity.Documento;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.remover.exception.DocumentoNaoEncontradoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.remover.exception.DocumentoNaoPertenceAMovimentacaoException;
import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
public class RemoverDocumentoMovimentacaoUseCaseImpl implements RemoverDocumentoMovimentacaoUseCase {

    private DocumentoDataProvider documentoDataProvider;

    private SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    @Override
    public void executar(RemoverDocumentoMovimentacaoInputData inputData) {
        validarDadosEntrada(inputData);
        Documento documento = buscarDocumento(inputData);
        validarSeDocumentoPertenceAMovimentacao(inputData, documento);
        removerArquivoTemporario(documento);
        removerDocumento(documento);
    }

    private void validarDadosEntrada(RemoverDocumentoMovimentacaoInputData inputData) {
        Validator.of(inputData)
            .validate(RemoverDocumentoMovimentacaoInputData::getId, Objects::nonNull, "O id é nulo")
            .validate(RemoverDocumentoMovimentacaoInputData::getMovimentacao, Objects::nonNull, "A movimentação é nula")
            .get();
    }

    private Documento buscarDocumento(RemoverDocumentoMovimentacaoInputData inputData) {
        return documentoDataProvider.buscarPorId(inputData.getId()).orElseThrow(DocumentoNaoEncontradoException::new);
    }

    private void validarSeDocumentoPertenceAMovimentacao(RemoverDocumentoMovimentacaoInputData inputData, Documento documento) {
        if (!documento.getMovimentacao().getId().equals(inputData.getMovimentacao())) {
            throw new DocumentoNaoPertenceAMovimentacaoException();
        }
    }

    private void removerArquivoTemporario(Documento documento) {
        if (Objects.nonNull(documento.getUriAnexo())) {
            sistemaDeArquivosIntegration.removeDefinitiveFile(Arquivo.builder().uri(documento.getUriAnexo()).build());
        }
    }

    private void removerDocumento(Documento documento) {
        documentoDataProvider.remover(documento.getId());
    }
}
