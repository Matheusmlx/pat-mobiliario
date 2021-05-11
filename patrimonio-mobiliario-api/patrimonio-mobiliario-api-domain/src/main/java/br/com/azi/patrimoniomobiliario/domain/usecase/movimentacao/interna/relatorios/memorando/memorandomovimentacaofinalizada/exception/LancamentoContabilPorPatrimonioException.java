package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.relatorios.memorando.memorandomovimentacaofinalizada.exception;

public class LancamentoContabilPorPatrimonioException extends RuntimeException {
    public LancamentoContabilPorPatrimonioException() { super("Não foi possível encontrar lançamnento contábil referente ao patrimônio"); }
}
