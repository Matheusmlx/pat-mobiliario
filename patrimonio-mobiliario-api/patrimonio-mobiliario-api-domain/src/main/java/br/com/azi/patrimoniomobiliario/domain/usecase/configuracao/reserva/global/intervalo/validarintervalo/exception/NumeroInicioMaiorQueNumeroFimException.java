package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.validarintervalo.exception;

public class NumeroInicioMaiorQueNumeroFimException extends RuntimeException {
    public NumeroInicioMaiorQueNumeroFimException() {
        super("O Número de início é maior que o número de fim.");
    }
}
