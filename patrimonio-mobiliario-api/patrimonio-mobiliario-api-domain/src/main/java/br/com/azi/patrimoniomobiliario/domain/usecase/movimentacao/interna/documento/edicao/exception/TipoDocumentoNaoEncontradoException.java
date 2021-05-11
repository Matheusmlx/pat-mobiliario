package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.edicao.exception;

public class TipoDocumentoNaoEncontradoException extends RuntimeException {
    public TipoDocumentoNaoEncontradoException() {
        super("Tipo de documento não encontrado");
    }
}
