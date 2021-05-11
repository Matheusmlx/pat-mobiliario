package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.devolucaoalmoxarifado.finalizacao.exception;

public class OrgaoInativoException extends RuntimeException {

    public OrgaoInativoException() {
        super("Órgão selecionado está inativo, por favor, selecione outro antes de finalizar.");
    }

}
