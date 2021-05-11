package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.inserir.exception;

public class IntervaloInvalidoException extends RuntimeException {

    public IntervaloInvalidoException() {
        super("O intervalo informado é inválido");
    }

}
