package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.remocao.exception;

public class IncorporacaoNaoEncontradoException extends RuntimeException {
    public IncorporacaoNaoEncontradoException() {
        super("Incorporação não encontrado!");
    }
}
