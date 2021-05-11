package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.cadastro.exception;

public class QuantidadeMaximaDeDocumentosAtigindoPorMovimentacaoException extends RuntimeException {
    public QuantidadeMaximaDeDocumentosAtigindoPorMovimentacaoException() {
        super("A quantidade máxima de documentos por movimentação foi atingida");
    }
}
