package br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.exception;

public class BuscarUnidadesOrganizacionaisException extends RuntimeException {
    public BuscarUnidadesOrganizacionaisException(String message) {
        super(message);
    }

    public BuscarUnidadesOrganizacionaisException(String message, Throwable cause) {
        super(message, cause);
    }
}
