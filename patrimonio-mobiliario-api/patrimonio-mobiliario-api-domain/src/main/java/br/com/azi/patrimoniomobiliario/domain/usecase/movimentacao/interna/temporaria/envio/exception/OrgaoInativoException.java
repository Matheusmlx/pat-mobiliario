package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.envio.exception;

public class OrgaoInativoException extends RuntimeException {

    public OrgaoInativoException() {
        super("Órgão selecionado está inativo. Por favor, selecione outro setor para finalizar.");
    }

}
