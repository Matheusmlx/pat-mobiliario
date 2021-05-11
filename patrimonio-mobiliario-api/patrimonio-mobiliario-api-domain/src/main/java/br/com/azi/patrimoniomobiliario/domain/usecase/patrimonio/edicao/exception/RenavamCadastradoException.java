package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao.exception;

public class RenavamCadastradoException extends RuntimeException {
    public RenavamCadastradoException() {
        super("Renavam já cadastrado");
    }
}
