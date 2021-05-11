package br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.calculardepreciacao.exception;

public class PatrimonioNaoEncontradoException extends RuntimeException {
    public PatrimonioNaoEncontradoException(Long patrimonioId) {
        super("O patrimônio #" + patrimonioId + " não foi encontrado.");
    }
}
