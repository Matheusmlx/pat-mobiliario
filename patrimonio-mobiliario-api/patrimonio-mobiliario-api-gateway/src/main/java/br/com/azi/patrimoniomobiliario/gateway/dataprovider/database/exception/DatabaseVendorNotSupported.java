package br.com.azi.patrimoniomobiliario.gateway.dataprovider.database.exception;

public class DatabaseVendorNotSupported extends RuntimeException {
    public DatabaseVendorNotSupported() {
        super("O banco de dados especificado não é suportado pela aplicação.");
    }
}
