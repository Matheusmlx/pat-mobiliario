package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.buscarproximonumero.exception;

public class ReservaNaoEncontradaException extends RuntimeException {
    public ReservaNaoEncontradaException() {
        super("Reserva não encontrada");
    }
}
