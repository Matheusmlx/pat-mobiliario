package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.buscarporid.exception;

public class ConvenioNaoEncontradoException extends RuntimeException {

    public ConvenioNaoEncontradoException() { super("Convênio não encontrado!"); }
}
