package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizarasync.exception;

public class FinalizarIncorporacaoAsyncException extends RuntimeException {
    public static final String DEFAULT_MESSAGE = "Ocorreu um erro ao realizar a finalização da incorporação.";

    public FinalizarIncorporacaoAsyncException(String message) {
        super(message);
    }

    public FinalizarIncorporacaoAsyncException(Throwable throwable) {
        super(DEFAULT_MESSAGE, throwable);
    }
}
