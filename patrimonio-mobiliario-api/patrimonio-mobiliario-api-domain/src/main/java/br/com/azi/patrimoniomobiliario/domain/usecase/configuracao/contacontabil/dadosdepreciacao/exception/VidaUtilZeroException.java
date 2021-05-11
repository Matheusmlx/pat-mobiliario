package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.dadosdepreciacao.exception;

public class VidaUtilZeroException extends RuntimeException{
    public VidaUtilZeroException() { super("Vida útil não deve ser zero.");}
}
