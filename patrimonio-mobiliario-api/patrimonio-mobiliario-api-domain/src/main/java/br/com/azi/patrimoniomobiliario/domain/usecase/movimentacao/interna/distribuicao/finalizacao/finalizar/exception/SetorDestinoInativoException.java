package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizar.exception;

public class SetorDestinoInativoException extends RuntimeException {
    public SetorDestinoInativoException() {
        super("Setor de destino selecionado está inativo, por favor, selecione outro antes de finalizar.");
    }
}
