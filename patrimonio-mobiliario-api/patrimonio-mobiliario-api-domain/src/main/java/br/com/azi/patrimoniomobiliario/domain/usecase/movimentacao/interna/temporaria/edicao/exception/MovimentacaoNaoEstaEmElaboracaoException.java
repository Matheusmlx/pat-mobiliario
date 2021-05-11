package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.edicao.exception;

public class MovimentacaoNaoEstaEmElaboracaoException extends RuntimeException {
    public MovimentacaoNaoEstaEmElaboracaoException() {
        super("A movimentação não está em elaboração");
    }

    public MovimentacaoNaoEstaEmElaboracaoException(String message) {
        super(message);
    }
}
