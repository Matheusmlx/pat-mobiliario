package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.finalizacao.exception;

public class MovimentacaoNaoEncontradaException extends RuntimeException {

    public MovimentacaoNaoEncontradaException() {
        super("Movimentação não encontrada");
    }

}
