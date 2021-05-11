package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.exclusao.exception;

public class IncorporacaoNaoEncontradaException extends RuntimeException {
    public IncorporacaoNaoEncontradaException() {
        super("Incorporação não encontrada!");
    }
}
