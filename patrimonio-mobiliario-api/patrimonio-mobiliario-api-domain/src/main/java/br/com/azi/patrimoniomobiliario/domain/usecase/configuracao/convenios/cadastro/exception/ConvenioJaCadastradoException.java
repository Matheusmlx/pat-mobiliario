package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.cadastro.exception;

public class ConvenioJaCadastradoException extends RuntimeException {
    public ConvenioJaCadastradoException (){ super("Convênio já cadastrado!");}
}
