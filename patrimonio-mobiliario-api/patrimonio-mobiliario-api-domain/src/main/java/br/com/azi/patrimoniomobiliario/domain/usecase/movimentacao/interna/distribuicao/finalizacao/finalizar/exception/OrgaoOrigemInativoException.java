package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizar.exception;

public class OrgaoOrigemInativoException extends RuntimeException {
    public OrgaoOrigemInativoException() {
        super("Órgão de origem selecionado está inativo, por favor, selecione outro antes de finalizar.");
    }
}
