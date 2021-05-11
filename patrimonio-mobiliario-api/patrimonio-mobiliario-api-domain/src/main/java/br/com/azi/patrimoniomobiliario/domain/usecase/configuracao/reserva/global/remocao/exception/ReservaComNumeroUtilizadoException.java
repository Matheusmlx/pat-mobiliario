package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.remocao.exception;

public class ReservaComNumeroUtilizadoException extends RuntimeException {
    public ReservaComNumeroUtilizadoException() {
        super("A reserva possui números que estão utilizados.");
    }
}
