package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.intervalo.relatorios.termoresponsabilidade.exception;

public class ReservaIntervaloNaoEncontradaException extends RuntimeException {
    public ReservaIntervaloNaoEncontradaException() {
        super("Intervalo não encontrado");
    }
}
