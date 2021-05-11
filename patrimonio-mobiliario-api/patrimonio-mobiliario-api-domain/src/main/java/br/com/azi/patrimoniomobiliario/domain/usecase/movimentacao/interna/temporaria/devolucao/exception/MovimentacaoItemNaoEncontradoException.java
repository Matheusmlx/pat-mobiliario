package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.devolucao.exception;

public class MovimentacaoItemNaoEncontradoException extends RuntimeException {
    public MovimentacaoItemNaoEncontradoException() {
        super("Movimentação item não encontrada.");
    }
}
