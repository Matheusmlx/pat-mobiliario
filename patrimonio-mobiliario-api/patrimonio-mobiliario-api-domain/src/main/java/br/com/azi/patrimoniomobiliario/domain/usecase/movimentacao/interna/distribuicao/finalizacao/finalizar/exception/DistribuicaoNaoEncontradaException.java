package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizar.exception;

public class DistribuicaoNaoEncontradaException extends RuntimeException {
    public DistribuicaoNaoEncontradaException() {
        super("Distribuição não encontrada.");
    }
}
