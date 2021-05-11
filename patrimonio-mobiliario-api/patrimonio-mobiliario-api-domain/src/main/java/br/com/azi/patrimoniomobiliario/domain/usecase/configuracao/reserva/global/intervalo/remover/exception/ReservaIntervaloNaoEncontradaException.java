package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.remover.exception;

public class ReservaIntervaloNaoEncontradaException extends RuntimeException {
    public ReservaIntervaloNaoEncontradaException() {
        super("Intervalo não encontrado");
    }
}
