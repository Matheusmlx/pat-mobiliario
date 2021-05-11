package br.com.azi.patrimoniomobiliario.domain.gateway.integration.exception;

public class UploadArquivoException extends RuntimeException {
    public UploadArquivoException(String mensagem) {
        super(mensagem);
    }

    public UploadArquivoException(String mensagem, Throwable throwable) {
        super(mensagem, throwable);
    }
}
