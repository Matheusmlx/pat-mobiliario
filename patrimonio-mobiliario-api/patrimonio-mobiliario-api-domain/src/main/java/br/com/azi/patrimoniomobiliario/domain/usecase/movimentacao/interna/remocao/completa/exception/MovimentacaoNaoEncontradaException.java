package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.remocao.completa.exception;

public class MovimentacaoNaoEncontradaException extends RuntimeException {
    public MovimentacaoNaoEncontradaException() {
        super("Movimentação não foi encontrada.");
    }
}
