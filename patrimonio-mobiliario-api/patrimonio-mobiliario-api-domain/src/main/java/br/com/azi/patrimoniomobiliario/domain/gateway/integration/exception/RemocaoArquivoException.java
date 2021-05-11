package br.com.azi.patrimoniomobiliario.domain.gateway.integration.exception;

public class RemocaoArquivoException extends RuntimeException {

    public RemocaoArquivoException(String mensagem, Throwable throwable){

        super(mensagem, throwable);
    }

    public RemocaoArquivoException(String mensagem){

        super(mensagem);
    }

}
