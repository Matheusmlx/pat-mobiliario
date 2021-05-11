package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.exclusao.exception;

public class IncorporacaoNaoEncontradaException extends RuntimeException {
    public IncorporacaoNaoEncontradaException() {
        super("A incorporação não foi encontrada.");
    }
}
