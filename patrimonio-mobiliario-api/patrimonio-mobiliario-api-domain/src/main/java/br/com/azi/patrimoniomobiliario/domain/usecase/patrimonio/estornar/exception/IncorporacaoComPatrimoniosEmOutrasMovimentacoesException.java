package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.estornar.exception;

public class IncorporacaoComPatrimoniosEmOutrasMovimentacoesException extends RuntimeException {
    public IncorporacaoComPatrimoniosEmOutrasMovimentacoesException() {
        super("A incorporação possui patrimônios em outras movimentações.");
    }
}
