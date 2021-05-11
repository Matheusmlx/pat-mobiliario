package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.cadastrar.exception;

public class IntervaloComQuantidadeIncorretaException extends RuntimeException {
    public IntervaloComQuantidadeIncorretaException() {
        super("Existe um intervalo com quantidade incorreta");
    }
}
