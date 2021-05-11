package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.devolucaoalmoxarifado.finalizacao.exception;

public class MovimentacaoNaoEhDevolucaoAlmoxarifadoException extends RuntimeException {

    public MovimentacaoNaoEhDevolucaoAlmoxarifadoException() {
        super("Movimentação não é do tipo devolução almoxarifado");
    }

}
