package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entresetores.finalizacao.exception;

public class SetorInativoException extends RuntimeException {

    public SetorInativoException() {
        super("Setor selecionado está inativo. Por favor, selecione outro setor para prosseguir.");
    }

    public SetorInativoException(String message) {
        super(message);
    }

}
