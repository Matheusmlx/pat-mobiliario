package br.com.azi.patrimoniomobiliario.gateway.integration.efornecedor.usecase.buscarporfiltro.exception;

public class BuscarFornecedoresException extends RuntimeException {
    public BuscarFornecedoresException(String message) {
        super(message);
    }

    public BuscarFornecedoresException(String message, Throwable cause) {
        super(message, cause);
    }
}
