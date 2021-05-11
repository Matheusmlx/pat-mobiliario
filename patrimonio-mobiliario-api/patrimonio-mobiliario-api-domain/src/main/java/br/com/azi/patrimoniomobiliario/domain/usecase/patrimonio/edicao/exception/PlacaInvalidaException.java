package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao.exception;

public class PlacaInvalidaException extends RuntimeException {
    public PlacaInvalidaException() {
        super("Placa inválida");
    }
}
