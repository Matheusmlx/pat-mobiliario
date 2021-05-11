package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizar.exception;

public class SetorOrigemInativoException extends RuntimeException {
    public SetorOrigemInativoException() {
        super("Setor de origem selecionado está inativo, por favor, selecione outro antes de finalizar.");
    }
}
