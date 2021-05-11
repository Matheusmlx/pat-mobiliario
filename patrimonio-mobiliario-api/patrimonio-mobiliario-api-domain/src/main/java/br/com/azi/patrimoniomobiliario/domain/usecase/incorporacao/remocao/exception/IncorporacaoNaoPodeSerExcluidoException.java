package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.remocao.exception;

public class IncorporacaoNaoPodeSerExcluidoException extends RuntimeException {
    public IncorporacaoNaoPodeSerExcluidoException() {
        super("Não é possível remover uma Incorporação finalizada.");
    }
}
