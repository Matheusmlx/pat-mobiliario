package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.edicao.exception;

public class ConcedenteNaoExisteException extends RuntimeException{
    public ConcedenteNaoExisteException() {
        super("Concedente não existe");
    }
}
