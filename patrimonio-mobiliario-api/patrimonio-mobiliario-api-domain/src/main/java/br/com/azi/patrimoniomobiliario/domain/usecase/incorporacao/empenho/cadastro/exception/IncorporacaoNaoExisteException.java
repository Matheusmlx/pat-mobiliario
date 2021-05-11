package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.cadastro.exception;

public class IncorporacaoNaoExisteException extends RuntimeException{
    public IncorporacaoNaoExisteException() {
        super("Incorporação não existe");
    }
}
