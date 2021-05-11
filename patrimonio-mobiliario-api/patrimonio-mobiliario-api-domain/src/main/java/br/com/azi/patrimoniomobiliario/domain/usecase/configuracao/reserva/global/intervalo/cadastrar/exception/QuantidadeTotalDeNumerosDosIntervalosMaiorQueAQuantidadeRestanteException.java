package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.cadastrar.exception;

public class QuantidadeTotalDeNumerosDosIntervalosMaiorQueAQuantidadeRestanteException extends RuntimeException {
    public QuantidadeTotalDeNumerosDosIntervalosMaiorQueAQuantidadeRestanteException() {
        super("A quantidade total de números dos intervalos é maior que a quantidade restante da reserva.");
    }
}
