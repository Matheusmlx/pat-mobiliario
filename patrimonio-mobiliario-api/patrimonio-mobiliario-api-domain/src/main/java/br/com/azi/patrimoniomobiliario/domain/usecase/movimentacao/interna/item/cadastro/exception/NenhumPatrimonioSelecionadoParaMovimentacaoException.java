package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.cadastro.exception;

public class NenhumPatrimonioSelecionadoParaMovimentacaoException extends RuntimeException {
    public NenhumPatrimonioSelecionadoParaMovimentacaoException() {
        super("Nenhum patrimônio selecionado para adicionar na movimentação.");
    }
}
