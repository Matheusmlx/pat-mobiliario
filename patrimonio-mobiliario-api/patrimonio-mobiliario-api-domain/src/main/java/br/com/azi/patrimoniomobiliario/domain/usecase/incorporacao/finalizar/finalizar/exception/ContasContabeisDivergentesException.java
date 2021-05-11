package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizar.exception;

public class ContasContabeisDivergentesException extends RuntimeException {
    public ContasContabeisDivergentesException() {
        super("Conta Contábil atual da Natureza de Despesa divergente da informada.");
    }
}
