package br.com.azi.patrimoniomobiliario.domain.gateway.integration.exception;

public class DownloadArquivoException extends RuntimeException {

    public DownloadArquivoException(String mensagem, Throwable throwable){

        super(mensagem, throwable);
    }

    public DownloadArquivoException(String mensagem){
        super(mensagem);
    }

}
