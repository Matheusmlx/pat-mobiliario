package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizarasync.exception;

public class IncorporacaoNaoEncontradaException extends FinalizarIncorporacaoAsyncException {
    public IncorporacaoNaoEncontradaException(Long id) {
        super("A incorporação #" + id + " não foi encontrada.");
    }
}
