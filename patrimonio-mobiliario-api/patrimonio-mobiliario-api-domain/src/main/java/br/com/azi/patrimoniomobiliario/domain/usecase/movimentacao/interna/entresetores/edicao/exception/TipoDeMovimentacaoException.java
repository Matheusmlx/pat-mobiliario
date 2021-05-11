package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entresetores.edicao.exception;

public class TipoDeMovimentacaoException extends RuntimeException {
    public TipoDeMovimentacaoException() {
        super("O tipo da movimentação não é válido.");
    }

    public TipoDeMovimentacaoException(String message) {
        super(message);
    }
}
