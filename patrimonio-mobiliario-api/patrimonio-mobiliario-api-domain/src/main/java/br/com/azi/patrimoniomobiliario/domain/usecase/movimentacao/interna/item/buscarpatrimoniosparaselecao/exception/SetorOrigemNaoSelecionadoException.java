package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.buscarpatrimoniosparaselecao.exception;

public class SetorOrigemNaoSelecionadoException extends RuntimeException {

    public SetorOrigemNaoSelecionadoException() {
        super("Setor de origem não selecionado");
    }

}
