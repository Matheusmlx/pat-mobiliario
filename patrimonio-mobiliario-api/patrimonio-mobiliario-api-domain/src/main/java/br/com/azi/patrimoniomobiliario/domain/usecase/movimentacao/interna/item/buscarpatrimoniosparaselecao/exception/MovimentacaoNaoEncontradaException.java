package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.buscarpatrimoniosparaselecao.exception;

public class MovimentacaoNaoEncontradaException extends RuntimeException {
    public MovimentacaoNaoEncontradaException() {
        super("Movimentação não foi encontrada.");
    }
}
