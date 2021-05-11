package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizar.exception;

public class OrgaoInativoException extends RuntimeException {
    public OrgaoInativoException() {
        super("O órgão escolhido foi inativado, por favor selecione outro!");
    }
}
