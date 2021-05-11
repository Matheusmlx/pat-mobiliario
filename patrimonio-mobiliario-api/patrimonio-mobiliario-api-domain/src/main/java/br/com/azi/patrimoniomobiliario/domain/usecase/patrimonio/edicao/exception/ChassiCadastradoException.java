package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao.exception;

public class ChassiCadastradoException extends RuntimeException {
    public ChassiCadastradoException() {
        super("Número de chassi já cadastrado");
    }
}
