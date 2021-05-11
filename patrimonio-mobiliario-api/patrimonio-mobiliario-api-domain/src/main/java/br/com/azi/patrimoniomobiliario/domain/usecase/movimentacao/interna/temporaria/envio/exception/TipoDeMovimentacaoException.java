package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.envio.exception;

public class TipoDeMovimentacaoException extends RuntimeException {

    public TipoDeMovimentacaoException() {
        super("Movimentação não é do tipo temporária");
    }

}
