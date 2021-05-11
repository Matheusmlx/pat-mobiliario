package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.editar.exception;

public class IncorporacaoNaoEncontradaException extends RuntimeException {
    public IncorporacaoNaoEncontradaException(){ super("Incorporação não encontrada!");}
}
