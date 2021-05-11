package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.edicao.exception;

public class ConvenioNaoEncontradoException extends RuntimeException {

    public ConvenioNaoEncontradoException() { super("Convênio não encontrado!"); }
}
