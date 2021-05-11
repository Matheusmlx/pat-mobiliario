package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.edicao.exception;

public class IncorporacaoNaoEstaEmElaboracaoException extends RuntimeException {
    public IncorporacaoNaoEstaEmElaboracaoException() {
        super("A incorporação não está em elaboração.");
    }
}
