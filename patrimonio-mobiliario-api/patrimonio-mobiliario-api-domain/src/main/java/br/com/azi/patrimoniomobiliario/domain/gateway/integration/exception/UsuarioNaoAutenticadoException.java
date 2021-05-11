package br.com.azi.patrimoniomobiliario.domain.gateway.integration.exception;

public class UsuarioNaoAutenticadoException extends RuntimeException{
    public UsuarioNaoAutenticadoException(String message){
        super(message);
    }
}
