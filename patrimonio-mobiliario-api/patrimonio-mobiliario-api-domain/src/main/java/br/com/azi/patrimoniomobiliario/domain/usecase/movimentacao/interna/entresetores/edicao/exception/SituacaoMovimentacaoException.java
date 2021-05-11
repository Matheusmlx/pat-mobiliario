package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entresetores.edicao.exception;

public class SituacaoMovimentacaoException extends RuntimeException {
    public SituacaoMovimentacaoException() {
        super("A situação da movimentação não permite esta operação.");
    }

    public SituacaoMovimentacaoException(String message) {
        super(message);
    }
}
