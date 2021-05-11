package br.com.azi.patrimoniomobiliario.domain.gateway.integration.exception;

public class PromoteArquivoException extends RuntimeException {

    public PromoteArquivoException(String mensagem) {
        super(mensagem);
    }

    public PromoteArquivoException(String mensagem, Throwable throwable) {
        super(mensagem, throwable);
    }
}
