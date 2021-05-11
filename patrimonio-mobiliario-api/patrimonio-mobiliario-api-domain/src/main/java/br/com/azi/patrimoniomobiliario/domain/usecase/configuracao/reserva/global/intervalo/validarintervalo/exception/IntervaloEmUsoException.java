package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.validarintervalo.exception;

public class IntervaloEmUsoException extends RuntimeException {
    public IntervaloEmUsoException() {
        super("O intervalo já está em uso");
    }
}
