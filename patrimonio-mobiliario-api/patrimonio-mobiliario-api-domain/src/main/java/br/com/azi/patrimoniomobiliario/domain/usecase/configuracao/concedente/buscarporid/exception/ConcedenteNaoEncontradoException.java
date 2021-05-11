package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.buscarporid.exception;

public class ConcedenteNaoEncontradoException extends RuntimeException {

    public ConcedenteNaoEncontradoException() { super("Concedente não encontrado!"); }
}
