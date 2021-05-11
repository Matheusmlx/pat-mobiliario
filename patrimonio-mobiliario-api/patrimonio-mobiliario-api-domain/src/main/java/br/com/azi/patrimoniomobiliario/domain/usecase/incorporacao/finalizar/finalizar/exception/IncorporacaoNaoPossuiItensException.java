package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizar.exception;

public class IncorporacaoNaoPossuiItensException extends RuntimeException {
    public IncorporacaoNaoPossuiItensException() {
        super("A incorporação não possui itens.");
    }
}
