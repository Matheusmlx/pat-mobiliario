package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.reabrir.exception;

public class MovimentacaoNaoEncontradaException extends RuntimeException {
    public MovimentacaoNaoEncontradaException() {
        super("A movimentação não foi encontrada.");
    }
}
