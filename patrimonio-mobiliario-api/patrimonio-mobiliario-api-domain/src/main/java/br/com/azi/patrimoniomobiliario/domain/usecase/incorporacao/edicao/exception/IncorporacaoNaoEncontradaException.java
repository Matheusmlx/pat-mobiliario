package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.edicao.exception;

public class IncorporacaoNaoEncontradaException extends RuntimeException {
    public IncorporacaoNaoEncontradaException() {
        super("Incorporação não encontrada");
    }
}
