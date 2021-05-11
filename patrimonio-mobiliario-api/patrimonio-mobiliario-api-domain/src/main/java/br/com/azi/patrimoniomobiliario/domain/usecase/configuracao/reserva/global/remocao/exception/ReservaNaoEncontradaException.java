package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.remocao.exception;

public class ReservaNaoEncontradaException extends RuntimeException {
    public ReservaNaoEncontradaException() {
        super("A reserva não foi encontrada");
    }
}
