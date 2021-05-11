package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.exclusao.exception;

public class DocumentoNaoEncontradoException extends RuntimeException {
    public DocumentoNaoEncontradoException() {
        super("Documento não encontrado!");
    }
}
