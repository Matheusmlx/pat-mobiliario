package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.remover.exception;

public class ReservaFinalizadaException extends RuntimeException {
    public ReservaFinalizadaException() {
        super("A reserva já está finalizada.");
    }
}
