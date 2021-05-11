package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao.exception;

public class ChassiInvalidoException extends RuntimeException {
    public ChassiInvalidoException() {
        super("Numeração do chassi inválida");
    }
}
