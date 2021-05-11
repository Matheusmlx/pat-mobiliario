package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.devolucao.exception;

public class NenhumPatrimonioSelecionadoParaDevolucaoException extends RuntimeException {
    public NenhumPatrimonioSelecionadoParaDevolucaoException() {
        super("Nenhum patrimônio selecionado para ser devolvido.");
    }
}
