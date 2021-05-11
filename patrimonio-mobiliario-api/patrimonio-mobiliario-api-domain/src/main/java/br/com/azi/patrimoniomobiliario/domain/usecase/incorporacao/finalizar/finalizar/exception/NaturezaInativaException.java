package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizar.exception;

public class NaturezaInativaException extends RuntimeException {
    public NaturezaInativaException() {
        super("A natureza de despesa escolhida foi inativada, por favor selecione outra!");
    }
}
