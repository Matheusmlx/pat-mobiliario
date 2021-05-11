package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.relatorios.memorando.memorandomovimentacaoemelaboracao.exception;

public class GerarMemorandoMovimentacaoEmElaboracaoException extends RuntimeException {
    public GerarMemorandoMovimentacaoEmElaboracaoException() { super("Não é possível gerar termo para movimentações finalizadas."); }
}
