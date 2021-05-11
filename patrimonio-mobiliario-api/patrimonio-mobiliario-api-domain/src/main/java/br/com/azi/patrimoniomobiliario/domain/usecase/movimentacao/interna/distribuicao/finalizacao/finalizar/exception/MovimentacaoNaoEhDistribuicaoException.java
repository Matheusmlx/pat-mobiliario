package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizar.exception;

public class MovimentacaoNaoEhDistribuicaoException extends RuntimeException {
    public MovimentacaoNaoEhDistribuicaoException() {
        super("A movimentação não é uma distribuição.");
    }
}
