package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.buscarpatrimoniosparaselecao.exception;

public class OrgaoOrigemNaoSelecionadoException extends RuntimeException {

    public OrgaoOrigemNaoSelecionadoException() {
        super("Órgão de origem não selecionado");
    }

}
