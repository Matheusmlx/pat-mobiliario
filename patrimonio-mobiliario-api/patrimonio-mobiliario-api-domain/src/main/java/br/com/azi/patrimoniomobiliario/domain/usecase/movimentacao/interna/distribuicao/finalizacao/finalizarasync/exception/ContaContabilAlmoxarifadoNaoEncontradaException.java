package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizarasync.exception;

public class ContaContabilAlmoxarifadoNaoEncontradaException extends FinalizarDistribuicaoAsyncException {
    public ContaContabilAlmoxarifadoNaoEncontradaException() {
        super("A conta contábil de almoxarifado não foi encontrada.");
    }
}
