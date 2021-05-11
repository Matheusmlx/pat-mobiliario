package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao.exception;

public class PlacaCadastradaException extends RuntimeException {
    public PlacaCadastradaException() {
        super("Placa já cadastrada");
    }
}
