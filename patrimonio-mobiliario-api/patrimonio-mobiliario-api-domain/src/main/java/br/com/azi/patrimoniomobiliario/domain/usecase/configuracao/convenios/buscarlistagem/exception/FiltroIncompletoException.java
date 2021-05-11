package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.buscarlistagem.exception;

public class FiltroIncompletoException extends RuntimeException {
    public FiltroIncompletoException(String message) {
        super(message);
    }
}
