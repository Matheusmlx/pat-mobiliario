package br.com.azi.patrimoniomobiliario.domain.gateway.integration.exception;

public class AlterarPropriedadeException extends RuntimeException {
    public AlterarPropriedadeException(String mensagem) {
        super(mensagem);
    }

    public AlterarPropriedadeException(String mensagem, Throwable throwable) {
        super(mensagem, throwable);
    }
}
