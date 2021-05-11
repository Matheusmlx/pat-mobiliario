package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao.exception;

public class RenavamInvalidoException extends RuntimeException {
    public RenavamInvalidoException() {
        super("Renavam inválido");
    }
}
