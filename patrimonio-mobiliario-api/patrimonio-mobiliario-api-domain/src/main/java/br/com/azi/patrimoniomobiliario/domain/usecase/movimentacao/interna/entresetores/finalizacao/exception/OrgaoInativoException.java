package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entresetores.finalizacao.exception;

public class OrgaoInativoException extends RuntimeException {
    public OrgaoInativoException() {
        super("Órgão selecionado está inativo. Por favor, selecione outro para prosseguir.");
    }

    public OrgaoInativoException(String message) {
        super(message);
    }
}
