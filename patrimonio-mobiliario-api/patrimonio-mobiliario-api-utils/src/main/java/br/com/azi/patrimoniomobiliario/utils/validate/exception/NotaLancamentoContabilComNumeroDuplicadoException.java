package br.com.azi.patrimoniomobiliario.utils.validate.exception;

public class NotaLancamentoContabilComNumeroDuplicadoException extends RuntimeException {
    public NotaLancamentoContabilComNumeroDuplicadoException() {
        super("O número da nota de lançamento contábil já está sendo utilizado.");
    }
}
