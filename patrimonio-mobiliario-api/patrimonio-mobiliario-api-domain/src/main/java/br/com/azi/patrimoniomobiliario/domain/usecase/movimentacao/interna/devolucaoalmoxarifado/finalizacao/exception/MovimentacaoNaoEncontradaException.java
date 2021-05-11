package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.devolucaoalmoxarifado.finalizacao.exception;

public class MovimentacaoNaoEncontradaException extends RuntimeException {

    public MovimentacaoNaoEncontradaException() {
        super("Movimentação não encontrada");
    }

}
