package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.remocao.exception;

public class MovimentacaoItemNaoEncontradaException extends RuntimeException {

    public MovimentacaoItemNaoEncontradaException() {
        super("Item movimentação não encontrado");
    }

}
