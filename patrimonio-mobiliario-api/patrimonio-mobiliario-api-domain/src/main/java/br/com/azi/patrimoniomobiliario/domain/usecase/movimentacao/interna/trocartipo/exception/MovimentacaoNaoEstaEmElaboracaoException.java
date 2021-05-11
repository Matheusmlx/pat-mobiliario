package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.trocartipo.exception;

public class MovimentacaoNaoEstaEmElaboracaoException extends RuntimeException {
    public MovimentacaoNaoEstaEmElaboracaoException() {
        super("Movimentação não está em elaboração.");
    }
}
