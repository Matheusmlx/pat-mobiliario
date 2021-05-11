package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.edicao.exception;

public class MovimentacaoNaoEncontradaException extends RuntimeException {
    public MovimentacaoNaoEncontradaException() {
        super("Movimentação não encontrada.");
    }

    public MovimentacaoNaoEncontradaException(String message) {
        super(message);
    }
}
