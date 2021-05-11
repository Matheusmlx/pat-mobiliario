package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizar.exception;

public class ReconhecimentoInativoException extends RuntimeException {
    public ReconhecimentoInativoException() {
        super("O reconhecimento escolhido foi inativado, por favor selecione outro!");
    }
}
