package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.finalizacao.exception;

public class MovimentacaoNaoEstaEmElaboracaoException extends RuntimeException {

    public MovimentacaoNaoEstaEmElaboracaoException() {
        super("Movimentação não está em elabaração, por isso não pode ser finalizada");
    }

}
