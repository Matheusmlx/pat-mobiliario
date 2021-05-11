package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.intervalo.relatorios.termoresponsabilidade.exception;

public class ReservaIntervaloNaoFinalizadaException extends RuntimeException {
    public ReservaIntervaloNaoFinalizadaException() {
        super("Intervalo ainda não finalizado");
    }
}
