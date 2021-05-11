package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.remocao.vazia.exception;

public class MovimentacaoNaoEstaEmElaboracaoException extends RuntimeException {
    public MovimentacaoNaoEstaEmElaboracaoException() {
        super("A movimentação não está Em Elaboração e por isso não pode ser removida");
    }
}
