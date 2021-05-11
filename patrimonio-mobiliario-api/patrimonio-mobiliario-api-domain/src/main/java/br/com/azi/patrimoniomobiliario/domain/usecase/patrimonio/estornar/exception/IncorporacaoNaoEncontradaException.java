package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.estornar.exception;

public class IncorporacaoNaoEncontradaException extends RuntimeException {
    public IncorporacaoNaoEncontradaException() {
        super("Incorporação não encontrada");
    }
}
