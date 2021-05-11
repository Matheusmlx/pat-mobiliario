package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.cadastro.exception;

public class CpfCnpjCadastradoException extends RuntimeException {
    public CpfCnpjCadastradoException(){ super("Existe um concedente cadastrado com este CPF/CNPJ.");}
}
