package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.buscartipo.exception;

public class MovimentacaoNaoEncontradaException extends RuntimeException {
    public MovimentacaoNaoEncontradaException() {
        super("Movimentação não foi encontrada.");
    }
}
