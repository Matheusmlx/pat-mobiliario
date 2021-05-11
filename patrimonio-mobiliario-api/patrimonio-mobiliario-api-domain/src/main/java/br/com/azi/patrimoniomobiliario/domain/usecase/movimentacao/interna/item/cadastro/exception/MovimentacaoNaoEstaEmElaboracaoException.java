package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.cadastro.exception;

public class MovimentacaoNaoEstaEmElaboracaoException extends RuntimeException {
    public MovimentacaoNaoEstaEmElaboracaoException() {
        super("A movimentação não está em elaboração.");
    }
}
