package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.dadosdepreciacao.exception;

public class PercentualResidualNuloException extends RuntimeException {
    public PercentualResidualNuloException(){ super("Percentual residual não pode ser nulo para conta do tipo depreciável."); }
}
