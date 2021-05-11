package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.finalizacao.exception;

public class ContaContabilNaoEncontradaException extends RuntimeException {

    public ContaContabilNaoEncontradaException() {
        super("A conta contábil não foi encontrada.");
    }

}
