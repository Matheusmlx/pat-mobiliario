package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.devolucaoalmoxarifado.finalizacao.exception;

public class MovimentacaoNaoEstaEmElaboracaoException extends RuntimeException {

    public MovimentacaoNaoEstaEmElaboracaoException() {
        super("Movimentação não está em elaboração e por isso não pode ser finalizada");
    }

}
