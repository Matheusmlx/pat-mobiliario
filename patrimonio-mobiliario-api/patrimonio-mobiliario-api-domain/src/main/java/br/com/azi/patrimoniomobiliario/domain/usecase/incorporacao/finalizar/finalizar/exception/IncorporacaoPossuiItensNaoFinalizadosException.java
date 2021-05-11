package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizar.exception;

public class IncorporacaoPossuiItensNaoFinalizadosException extends RuntimeException {
    public IncorporacaoPossuiItensNaoFinalizadosException() {
        super("A incorporação possui itens que não estão finalizados.");
    }
}
