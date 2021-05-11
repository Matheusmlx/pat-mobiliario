package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.dadosdepreciacao.exception;

public class PercentualResidualZeroException extends RuntimeException{
    public PercentualResidualZeroException() { super("Percentual residual não deve ser menor ou igual a zero.");}
}
