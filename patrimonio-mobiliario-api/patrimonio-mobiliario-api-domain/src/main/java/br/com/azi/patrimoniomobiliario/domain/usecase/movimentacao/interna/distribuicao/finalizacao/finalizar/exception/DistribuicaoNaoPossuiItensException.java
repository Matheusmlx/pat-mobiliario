package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizar.exception;

public class DistribuicaoNaoPossuiItensException extends RuntimeException {
    public DistribuicaoNaoPossuiItensException() {
        super("A distribuição não possui itens adicionados.");
    }
}
