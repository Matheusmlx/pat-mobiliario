package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizar.exception;

public class ContaContabilAlmoxarifadoInativaException extends RuntimeException {
    public ContaContabilAlmoxarifadoInativaException() {
        super("A conta contábil de almoxarifado está inativa.");
    }
}
