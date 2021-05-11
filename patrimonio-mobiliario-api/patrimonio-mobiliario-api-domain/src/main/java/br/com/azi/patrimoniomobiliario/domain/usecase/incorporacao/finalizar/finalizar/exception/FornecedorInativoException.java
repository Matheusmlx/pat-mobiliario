package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizar.exception;

public class FornecedorInativoException extends RuntimeException {
    public FornecedorInativoException() {
        super("O fornecedor escolhido foi inativado, por favor selecione outro!");
    }
}
