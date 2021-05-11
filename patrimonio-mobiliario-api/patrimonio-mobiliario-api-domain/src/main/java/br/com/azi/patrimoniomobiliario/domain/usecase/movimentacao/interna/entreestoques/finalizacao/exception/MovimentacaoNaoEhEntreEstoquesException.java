package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.finalizacao.exception;

public class MovimentacaoNaoEhEntreEstoquesException extends RuntimeException {

    public MovimentacaoNaoEhEntreEstoquesException() {
        super("Movimentação não é do tipo entre estoques");
    }

}
