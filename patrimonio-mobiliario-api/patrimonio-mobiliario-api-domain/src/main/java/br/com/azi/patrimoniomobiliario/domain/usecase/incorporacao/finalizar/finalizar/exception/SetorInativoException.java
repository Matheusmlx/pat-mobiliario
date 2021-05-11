package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizar.exception;

public class SetorInativoException extends RuntimeException {
    public SetorInativoException() {
        super("O setor escolhido foi inativado, por favor selecione outro!");
    }
}
