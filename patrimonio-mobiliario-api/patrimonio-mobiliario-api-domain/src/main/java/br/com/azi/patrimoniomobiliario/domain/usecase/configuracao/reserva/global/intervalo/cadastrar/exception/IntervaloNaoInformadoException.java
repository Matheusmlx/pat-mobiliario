package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.cadastrar.exception;

public class IntervaloNaoInformadoException extends RuntimeException {
    public IntervaloNaoInformadoException() {
        super("O intervalo a ser reservado não foi informado");
    }
}
