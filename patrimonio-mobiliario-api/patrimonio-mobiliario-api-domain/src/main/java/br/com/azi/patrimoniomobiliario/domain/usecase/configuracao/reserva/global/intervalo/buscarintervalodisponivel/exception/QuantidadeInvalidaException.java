package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.buscarintervalodisponivel.exception;

public class QuantidadeInvalidaException extends RuntimeException {
    public QuantidadeInvalidaException() {
        super("A quantidade a ser reservada deve ser maior que 0");
    }
}
