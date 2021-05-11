package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.visualizacao.exception;

public class MovimentacaoNaoEncontrada extends RuntimeException {

    public MovimentacaoNaoEncontrada() {
        super("Movimentação não encontrada");
    }

}
