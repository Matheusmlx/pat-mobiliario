package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.dadosdepreciacao.exception;

public class ConfigContaContabilNaoEncontradaException extends RuntimeException{
    public ConfigContaContabilNaoEncontradaException() { super("Não foi possível encontrar a configuração da conta contábil.");}
}
