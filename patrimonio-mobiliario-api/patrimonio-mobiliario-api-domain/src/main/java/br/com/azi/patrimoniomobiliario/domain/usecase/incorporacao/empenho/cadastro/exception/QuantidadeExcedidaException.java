package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.cadastro.exception;

public class QuantidadeExcedidaException extends RuntimeException {
    public QuantidadeExcedidaException() {
        super("Não é possível cadastrar mais que 10 empenhos!");
    }
}
