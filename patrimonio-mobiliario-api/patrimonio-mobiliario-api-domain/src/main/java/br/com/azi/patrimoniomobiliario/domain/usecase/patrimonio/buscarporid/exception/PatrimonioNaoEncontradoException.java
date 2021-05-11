package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.buscarporid.exception;

public class PatrimonioNaoEncontradoException extends RuntimeException {
    public PatrimonioNaoEncontradoException() {
        super("Patrimônio não encontrado.");
    }
}
