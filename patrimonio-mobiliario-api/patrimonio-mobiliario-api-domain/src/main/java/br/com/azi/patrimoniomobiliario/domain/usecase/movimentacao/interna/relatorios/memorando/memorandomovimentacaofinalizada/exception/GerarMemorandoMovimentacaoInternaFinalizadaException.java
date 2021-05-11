package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.relatorios.memorando.memorandomovimentacaofinalizada.exception;

public class GerarMemorandoMovimentacaoInternaFinalizadaException extends RuntimeException {
    public GerarMemorandoMovimentacaoInternaFinalizadaException(String msg) {
        super(msg);
    }

    public GerarMemorandoMovimentacaoInternaFinalizadaException() {
        super( "Não foi possível gerar memorando!");
    }
}
