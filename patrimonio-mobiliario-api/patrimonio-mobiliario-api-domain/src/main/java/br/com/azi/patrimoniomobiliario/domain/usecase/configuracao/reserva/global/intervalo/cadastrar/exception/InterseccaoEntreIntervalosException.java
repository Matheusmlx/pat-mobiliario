package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.cadastrar.exception;

public class InterseccaoEntreIntervalosException extends RuntimeException {
    public InterseccaoEntreIntervalosException() {
        super("Existem intervalos com intersecção.");
    }
}
