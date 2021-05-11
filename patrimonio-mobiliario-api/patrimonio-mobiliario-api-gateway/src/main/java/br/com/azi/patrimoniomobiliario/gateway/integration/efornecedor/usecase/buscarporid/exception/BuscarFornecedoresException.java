package br.com.azi.patrimoniomobiliario.gateway.integration.efornecedor.usecase.buscarporid.exception;

public class BuscarFornecedoresException extends RuntimeException {
    public BuscarFornecedoresException(String message) {
        super(message);
    }

    public BuscarFornecedoresException(String message, Throwable cause) {
        super(message, cause);
    }
}
