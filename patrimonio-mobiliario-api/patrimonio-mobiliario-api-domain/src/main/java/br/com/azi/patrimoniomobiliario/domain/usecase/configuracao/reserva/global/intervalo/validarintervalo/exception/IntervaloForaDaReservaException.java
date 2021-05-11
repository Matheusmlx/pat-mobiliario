package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.validarintervalo.exception;

public class IntervaloForaDaReservaException extends RuntimeException {
    public IntervaloForaDaReservaException() {
        super("O intervalo informado possui número fora da reserva");
    }
}
