package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.edicao.exception;

public class DocumentoNaoEncontradoException extends RuntimeException {
    public DocumentoNaoEncontradoException() {
        super("Documento não foi encontrado.");
    }
}
