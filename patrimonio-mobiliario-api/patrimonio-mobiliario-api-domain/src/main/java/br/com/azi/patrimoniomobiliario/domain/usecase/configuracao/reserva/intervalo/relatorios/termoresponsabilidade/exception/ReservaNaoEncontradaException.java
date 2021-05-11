package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.intervalo.relatorios.termoresponsabilidade.exception;

public class ReservaNaoEncontradaException extends RuntimeException {

    public ReservaNaoEncontradaException() {
        super("Reserva não encontrada");
    }

}
