package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.buscarproximonumero.exception;

public class ReservaCompletaException extends RuntimeException {
    public ReservaCompletaException() {
        super("Não há números a serem utilizados na reserva.");
    }
}
