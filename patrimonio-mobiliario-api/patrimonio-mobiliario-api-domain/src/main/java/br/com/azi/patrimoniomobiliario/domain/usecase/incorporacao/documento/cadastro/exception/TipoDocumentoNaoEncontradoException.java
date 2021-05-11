package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.cadastro.exception;

public class TipoDocumentoNaoEncontradoException extends RuntimeException {

    public TipoDocumentoNaoEncontradoException() {
        super("Tipo de documento não encontrado!");
    }
}
