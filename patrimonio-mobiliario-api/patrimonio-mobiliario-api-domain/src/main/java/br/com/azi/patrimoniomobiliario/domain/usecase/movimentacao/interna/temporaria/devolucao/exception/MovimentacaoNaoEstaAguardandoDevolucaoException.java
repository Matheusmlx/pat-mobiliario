package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.devolucao.exception;

public class MovimentacaoNaoEstaAguardandoDevolucaoException extends RuntimeException {
    public MovimentacaoNaoEstaAguardandoDevolucaoException() {
        super("A movimentação não está aguardando devolução.");
    }
}
