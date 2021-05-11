package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.edicao.exception;

public class SituacaoReservaException extends RuntimeException {
    public SituacaoReservaException() {
        super("Essa reserva não está em elaboração");
    }
}
