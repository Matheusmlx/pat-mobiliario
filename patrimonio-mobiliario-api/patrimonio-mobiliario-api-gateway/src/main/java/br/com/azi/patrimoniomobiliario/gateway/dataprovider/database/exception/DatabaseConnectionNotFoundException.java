package br.com.azi.patrimoniomobiliario.gateway.dataprovider.database.exception;

public class DatabaseConnectionNotFoundException extends RuntimeException {
    public DatabaseConnectionNotFoundException(Throwable e) {
        super("Ocorreu um erro ao recuperar a conexão com o banco de dados.", e);
    }
}
