package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizarerro.exception;

public class DistribuicaoNaoEncontradaException extends RuntimeException {
    public DistribuicaoNaoEncontradaException() {
        super("Distribuição não encontrada.");
    }
}
