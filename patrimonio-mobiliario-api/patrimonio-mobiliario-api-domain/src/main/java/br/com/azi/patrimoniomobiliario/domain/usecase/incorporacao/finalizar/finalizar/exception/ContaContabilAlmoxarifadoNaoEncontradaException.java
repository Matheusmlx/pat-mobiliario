package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizar.exception;

public class ContaContabilAlmoxarifadoNaoEncontradaException extends RuntimeException {
    public ContaContabilAlmoxarifadoNaoEncontradaException() {
        super("A conta contábil de almoxarifado não foi encontrada.");
    }
}
