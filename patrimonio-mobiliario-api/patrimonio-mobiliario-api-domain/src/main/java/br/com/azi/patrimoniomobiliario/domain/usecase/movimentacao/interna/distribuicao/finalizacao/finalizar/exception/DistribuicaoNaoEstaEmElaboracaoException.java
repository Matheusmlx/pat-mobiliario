package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizar.exception;

public class DistribuicaoNaoEstaEmElaboracaoException extends RuntimeException {
    public DistribuicaoNaoEstaEmElaboracaoException() {
        super("A distribuição não está em elaboração e por isso não pode ser finalizada.");
    }
}
