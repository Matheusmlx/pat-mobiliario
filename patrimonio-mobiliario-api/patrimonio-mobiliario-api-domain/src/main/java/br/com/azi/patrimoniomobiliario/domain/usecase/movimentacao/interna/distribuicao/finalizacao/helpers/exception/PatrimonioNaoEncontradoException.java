package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.helpers.exception;

import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizarasync.exception.FinalizarDistribuicaoAsyncException;

public class PatrimonioNaoEncontradoException extends FinalizarDistribuicaoAsyncException {
    public PatrimonioNaoEncontradoException(Long id) {
        super("O patrimônio com id #" + id + " não foi encontrado.");
    }
}
