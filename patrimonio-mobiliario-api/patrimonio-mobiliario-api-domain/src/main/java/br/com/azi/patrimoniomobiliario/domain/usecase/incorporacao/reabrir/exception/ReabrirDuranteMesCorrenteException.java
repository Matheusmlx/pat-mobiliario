package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.reabrir.exception;

public class ReabrirDuranteMesCorrenteException extends RuntimeException {
    public ReabrirDuranteMesCorrenteException() {
        super("Não é possível reabrir incorporação fora do mês corrente");
    }
}
