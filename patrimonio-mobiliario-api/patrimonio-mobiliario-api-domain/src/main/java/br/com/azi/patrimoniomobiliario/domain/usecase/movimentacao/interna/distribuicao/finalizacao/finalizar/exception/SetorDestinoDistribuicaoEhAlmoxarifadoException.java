package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizar.exception;

public class SetorDestinoDistribuicaoEhAlmoxarifadoException extends RuntimeException {
    public SetorDestinoDistribuicaoEhAlmoxarifadoException() {
        super("O setor de destino selecionado é um setor almoxarifado.");
    }
}
