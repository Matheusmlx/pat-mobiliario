package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizar.exception;

public class ContaContabilAlmoxarifadoInativaException extends RuntimeException {
    public ContaContabilAlmoxarifadoInativaException() {
        super("A conta contábil de almoxarifado está inativa.");
    }
}
