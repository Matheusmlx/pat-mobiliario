package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.cadastro.exception;

public class CpfCnpjIncorretoException extends RuntimeException {
   public CpfCnpjIncorretoException() { super("O CPF/CNPJ inserido está incorreto.");}
}
