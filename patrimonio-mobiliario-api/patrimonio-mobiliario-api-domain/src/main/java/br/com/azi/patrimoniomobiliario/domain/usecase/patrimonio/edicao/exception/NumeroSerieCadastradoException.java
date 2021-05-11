package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao.exception;

public class NumeroSerieCadastradoException extends RuntimeException {
    public NumeroSerieCadastradoException() {
        super("Número série já cadastrado");
    }
}
