package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizarasync.exception;

public class DistribuicaoNaoEncontradaException extends FinalizarDistribuicaoAsyncException {
    public DistribuicaoNaoEncontradaException(Long id) {
        super("A distribuição com id #" + id + " não foi encontrada.");
    }
}
