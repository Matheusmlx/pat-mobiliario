package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.edicao.exception;

public class IntervaloInvalidoException extends RuntimeException {
    public IntervaloInvalidoException() {
        super("O intervalo informado é inválido");
    }
}
