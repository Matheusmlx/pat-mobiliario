package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.remocao.exception;

public class PatrimonioNaoEncontradoException extends RuntimeException {

    public PatrimonioNaoEncontradoException() {
        super("Patrimônio não encontrado");
    }

}
