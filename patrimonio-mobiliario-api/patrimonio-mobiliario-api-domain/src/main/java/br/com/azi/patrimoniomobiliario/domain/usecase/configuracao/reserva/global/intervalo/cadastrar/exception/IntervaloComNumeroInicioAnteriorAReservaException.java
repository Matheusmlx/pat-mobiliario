package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.cadastrar.exception;

public class IntervaloComNumeroInicioAnteriorAReservaException extends RuntimeException {
    public IntervaloComNumeroInicioAnteriorAReservaException() {
        super("Existe um intervalo com número de início anterior ao da reserva.");
    }
}
