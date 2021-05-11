package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.remover.exception;

public class IntervaloJaEstaFinalizadoException extends RuntimeException {
    public IntervaloJaEstaFinalizadoException() {
        super("Este intervalo não pode ser excluído, pois já está finalizado");
    }
}
