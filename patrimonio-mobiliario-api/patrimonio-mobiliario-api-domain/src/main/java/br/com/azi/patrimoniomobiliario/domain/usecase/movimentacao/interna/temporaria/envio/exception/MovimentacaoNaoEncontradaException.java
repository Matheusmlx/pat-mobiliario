package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.envio.exception;

public class MovimentacaoNaoEncontradaException extends RuntimeException {

    public MovimentacaoNaoEncontradaException() {
        super("Movimentação não encontrada");
    }

}
