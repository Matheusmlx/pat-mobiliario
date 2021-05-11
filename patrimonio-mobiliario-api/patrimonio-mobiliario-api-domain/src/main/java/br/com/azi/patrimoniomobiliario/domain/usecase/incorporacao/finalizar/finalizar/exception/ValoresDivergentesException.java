package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizar.exception;

public class ValoresDivergentesException extends RuntimeException {
    public ValoresDivergentesException() {
        super("Valor total dos bens divergente do valor da nota fiscal.");
    }
}
