package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.cadastro.exception;

public class MovimentacaoNaoEncontradaException extends RuntimeException {
    public MovimentacaoNaoEncontradaException() {
        super("Movimentação não foi encontrada.");
    }
}
