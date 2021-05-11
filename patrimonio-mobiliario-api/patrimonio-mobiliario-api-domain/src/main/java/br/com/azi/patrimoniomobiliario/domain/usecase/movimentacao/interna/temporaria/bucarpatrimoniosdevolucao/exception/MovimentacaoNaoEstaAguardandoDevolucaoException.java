package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.bucarpatrimoniosdevolucao.exception;

public class MovimentacaoNaoEstaAguardandoDevolucaoException extends RuntimeException {
    public MovimentacaoNaoEstaAguardandoDevolucaoException() {
        super("A movimentação não está aguardando devolução.");
    }
}
