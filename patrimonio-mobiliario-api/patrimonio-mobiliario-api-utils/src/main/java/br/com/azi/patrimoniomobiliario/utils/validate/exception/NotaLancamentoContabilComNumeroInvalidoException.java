package br.com.azi.patrimoniomobiliario.utils.validate.exception;

public class NotaLancamentoContabilComNumeroInvalidoException extends RuntimeException {

    public NotaLancamentoContabilComNumeroInvalidoException() {
        super("O número da nota de lançamento contábil é inválido.");
    }
}
