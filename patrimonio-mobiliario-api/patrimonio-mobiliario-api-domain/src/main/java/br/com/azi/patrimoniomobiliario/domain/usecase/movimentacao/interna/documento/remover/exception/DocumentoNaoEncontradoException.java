package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.remover.exception;

public class DocumentoNaoEncontradoException extends RuntimeException {
    public DocumentoNaoEncontradoException() {
        super("Documento não encontrado!");
    }
}
