package br.com.azi.patrimoniomobiliario.utils.string.exception;

public class CpfCnpjIncorretoException extends RuntimeException {
   public CpfCnpjIncorretoException() { super("O CPF/CNPJ inserido está incorreto.");}
}
