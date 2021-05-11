package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.cadastrar.exception;

public class IntervaloComNumeroFimPosteriorAReservaException extends RuntimeException {
    public IntervaloComNumeroFimPosteriorAReservaException() {
        super("Existe um intervalo com número de fim posterior ao da reserva.");
    }
}
