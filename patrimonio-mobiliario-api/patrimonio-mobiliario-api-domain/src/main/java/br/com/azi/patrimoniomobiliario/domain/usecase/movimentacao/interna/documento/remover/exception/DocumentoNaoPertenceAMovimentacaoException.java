package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.remover.exception;

public class DocumentoNaoPertenceAMovimentacaoException extends RuntimeException {
    public DocumentoNaoPertenceAMovimentacaoException() {
        super("Este documento não pertence a movimentação informada");
    }
}
