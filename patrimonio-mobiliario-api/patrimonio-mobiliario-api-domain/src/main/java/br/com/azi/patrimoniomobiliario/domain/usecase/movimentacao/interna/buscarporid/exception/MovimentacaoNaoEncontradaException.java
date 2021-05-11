package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.buscarporid.exception;

public class MovimentacaoNaoEncontradaException extends RuntimeException {
    public MovimentacaoNaoEncontradaException() {
        super("Movimentação não encontrada");
    }
}
