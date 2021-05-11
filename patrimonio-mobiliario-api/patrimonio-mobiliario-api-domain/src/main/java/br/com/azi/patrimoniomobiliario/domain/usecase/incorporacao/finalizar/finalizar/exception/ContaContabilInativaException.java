package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizar.exception;

public class ContaContabilInativaException extends RuntimeException {
    public ContaContabilInativaException() {
        super("A conta contábil escolhida foi inativada, por favor selecione outra!");
    }
}
