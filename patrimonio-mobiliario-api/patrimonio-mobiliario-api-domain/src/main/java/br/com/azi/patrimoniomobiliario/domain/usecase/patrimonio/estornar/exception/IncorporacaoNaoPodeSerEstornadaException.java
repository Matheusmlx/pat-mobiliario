package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.estornar.exception;

public class IncorporacaoNaoPodeSerEstornadaException extends RuntimeException {
    public IncorporacaoNaoPodeSerEstornadaException() {
        super("A situação da incorporação não permite estornar patrimônios.");
    }
}
