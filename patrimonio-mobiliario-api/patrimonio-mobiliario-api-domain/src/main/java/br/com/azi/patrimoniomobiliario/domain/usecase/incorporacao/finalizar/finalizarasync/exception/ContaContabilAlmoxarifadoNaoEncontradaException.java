package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizarasync.exception;

public class ContaContabilAlmoxarifadoNaoEncontradaException extends FinalizarIncorporacaoAsyncException {
    public ContaContabilAlmoxarifadoNaoEncontradaException() {
        super("A conta contábil de almoxarifado não foi encontrada.");
    }
}
