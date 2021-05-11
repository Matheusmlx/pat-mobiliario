package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizarasync.exception;

public class FinalizarDistribuicaoAsyncException extends RuntimeException {
    public static final String DEFAULT_MESSAGE = "Ocorreu um erro ao realizar a finalização da distribuição.";

    public FinalizarDistribuicaoAsyncException(String message) {
        super(message);
    }

}
