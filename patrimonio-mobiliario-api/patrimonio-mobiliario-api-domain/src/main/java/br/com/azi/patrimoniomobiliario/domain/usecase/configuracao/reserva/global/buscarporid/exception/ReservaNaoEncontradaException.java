package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.buscarporid.exception;

public class ReservaNaoEncontradaException extends RuntimeException {
    public ReservaNaoEncontradaException() {
        super("Reserva não encontrada");
    }
}
