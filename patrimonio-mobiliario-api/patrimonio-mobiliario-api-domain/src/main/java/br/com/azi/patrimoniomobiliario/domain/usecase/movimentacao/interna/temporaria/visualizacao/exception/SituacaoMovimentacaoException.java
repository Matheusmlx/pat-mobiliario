package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.visualizacao.exception;

public class SituacaoMovimentacaoException extends RuntimeException {

    public SituacaoMovimentacaoException() {
        super("A situação da movimentação não permite esta operação.");
    }

}
