package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao.exception;

public class PatrimonioNaoExisteException extends RuntimeException {
    public PatrimonioNaoExisteException() {
        super("Patrimônio não existe");
    }
}
