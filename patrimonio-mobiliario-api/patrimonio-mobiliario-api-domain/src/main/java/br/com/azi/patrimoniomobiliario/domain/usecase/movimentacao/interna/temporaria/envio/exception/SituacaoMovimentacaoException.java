package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.envio.exception;

public class SituacaoMovimentacaoException extends RuntimeException {

    public SituacaoMovimentacaoException() {
        super("Movimentação não está em elaboração e por isso não pode ser enviada");
    }

}
