package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.finalizar.exception;

public class ReservaFinalizadaException extends RuntimeException {
    public ReservaFinalizadaException() {
        super("Essa reserva já foi finalizada");
    }
}
