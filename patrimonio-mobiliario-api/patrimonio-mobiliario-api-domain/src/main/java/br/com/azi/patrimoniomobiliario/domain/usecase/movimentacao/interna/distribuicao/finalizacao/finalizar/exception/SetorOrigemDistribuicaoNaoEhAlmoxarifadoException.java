package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizar.exception;

public class SetorOrigemDistribuicaoNaoEhAlmoxarifadoException extends RuntimeException {
    public SetorOrigemDistribuicaoNaoEhAlmoxarifadoException() {
        super("O setor de origem selecionado não é um setor almoxarifado.");
    }
}
