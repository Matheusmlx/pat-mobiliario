package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.edicao.exception;

public class DocumentoJaCadastradoComMesmoNumeroETipoException extends RuntimeException {
    public DocumentoJaCadastradoComMesmoNumeroETipoException() {
        super("Já existe um documento com este número e tipo.");
    }
}
