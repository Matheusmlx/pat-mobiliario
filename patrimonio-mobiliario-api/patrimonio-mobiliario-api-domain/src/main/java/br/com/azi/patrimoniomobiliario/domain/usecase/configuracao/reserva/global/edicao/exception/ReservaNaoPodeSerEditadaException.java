package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.edicao.exception;

public class ReservaNaoPodeSerEditadaException extends RuntimeException {
    public ReservaNaoPodeSerEditadaException() {
        super("Essa reserva não pode ser alterada, pois possui intervalos cadastrados");
    }
}
