package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.cadastro.exception;

public class NumeroTipoUnicoException extends RuntimeException {
    public NumeroTipoUnicoException() {
        super("Já existe um documento com este número e tipo.");
    }
}
