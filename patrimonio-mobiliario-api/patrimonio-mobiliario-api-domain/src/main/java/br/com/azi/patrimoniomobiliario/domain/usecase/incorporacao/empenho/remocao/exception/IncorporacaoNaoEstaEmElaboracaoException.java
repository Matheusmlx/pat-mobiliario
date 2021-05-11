package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.remocao.exception;

public class IncorporacaoNaoEstaEmElaboracaoException extends RuntimeException {
    public IncorporacaoNaoEstaEmElaboracaoException() {
        super("A incorporação não está em elaboração.");
    }
}
