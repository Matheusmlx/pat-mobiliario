package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.edicao.exception;

public class ConvenioJaCadastradoException extends RuntimeException {
    public ConvenioJaCadastradoException (){ super("Convênio já cadastrado!");}
}
