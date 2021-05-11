package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.edicao.exception;

public class IncorporacaoNaoExisteException extends RuntimeException{
    public IncorporacaoNaoExisteException() {
        super("Incorporação não existe");
    }
}
