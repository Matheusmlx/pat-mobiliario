package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao.exception;

public class MotorCadastradoException extends RuntimeException {
    public MotorCadastradoException() {
        super("Número do motor já cadastrado");
    }
}
