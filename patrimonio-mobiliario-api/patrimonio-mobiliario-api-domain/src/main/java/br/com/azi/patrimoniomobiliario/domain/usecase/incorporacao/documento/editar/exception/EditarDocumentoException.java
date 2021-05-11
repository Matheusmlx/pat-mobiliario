package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.editar.exception;

public class EditarDocumentoException extends RuntimeException {
    public EditarDocumentoException() {
        super("Não foi possível editar o documento!");
    }
}
