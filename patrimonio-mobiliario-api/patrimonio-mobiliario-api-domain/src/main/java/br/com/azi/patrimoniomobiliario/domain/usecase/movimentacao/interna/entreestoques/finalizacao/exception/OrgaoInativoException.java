package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.finalizacao.exception;

public class OrgaoInativoException extends RuntimeException {

    public OrgaoInativoException() {
        super("Órgão selecionado está inativo, por favor, selecione outro antes de finalizar.");
    }

}
