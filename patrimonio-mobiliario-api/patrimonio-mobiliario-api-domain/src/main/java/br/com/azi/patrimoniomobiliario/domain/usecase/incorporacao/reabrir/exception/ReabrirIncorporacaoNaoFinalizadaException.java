package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.reabrir.exception;

public class ReabrirIncorporacaoNaoFinalizadaException extends RuntimeException {
    public ReabrirIncorporacaoNaoFinalizadaException() {
        super("Não é possível reabrir incorporações que não estejam finalizadas.");
    }
}
