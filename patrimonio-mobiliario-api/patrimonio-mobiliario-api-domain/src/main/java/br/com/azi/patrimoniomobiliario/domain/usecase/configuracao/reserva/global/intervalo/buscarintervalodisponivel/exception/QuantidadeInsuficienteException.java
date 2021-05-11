package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.buscarintervalodisponivel.exception;

public class QuantidadeInsuficienteException extends RuntimeException {
    public QuantidadeInsuficienteException() {
        super("A reserva nao possui quantidade de números suficientes");
    }
}
