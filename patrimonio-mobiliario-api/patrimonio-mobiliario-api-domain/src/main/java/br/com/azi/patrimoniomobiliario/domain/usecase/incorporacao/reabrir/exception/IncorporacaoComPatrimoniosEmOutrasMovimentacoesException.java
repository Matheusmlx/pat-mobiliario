package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.reabrir.exception;

public class IncorporacaoComPatrimoniosEmOutrasMovimentacoesException extends RuntimeException {
    public IncorporacaoComPatrimoniosEmOutrasMovimentacoesException() {
        super("A incorporação possui patrimônios em outras movimentações.");
    }
}
