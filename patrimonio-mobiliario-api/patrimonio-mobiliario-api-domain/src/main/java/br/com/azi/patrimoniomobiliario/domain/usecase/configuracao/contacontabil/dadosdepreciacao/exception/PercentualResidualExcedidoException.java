package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.dadosdepreciacao.exception;

public class PercentualResidualExcedidoException extends RuntimeException{
    public PercentualResidualExcedidoException() { super("Percentual residual não deve ser maior que 99,99%.");}
}
