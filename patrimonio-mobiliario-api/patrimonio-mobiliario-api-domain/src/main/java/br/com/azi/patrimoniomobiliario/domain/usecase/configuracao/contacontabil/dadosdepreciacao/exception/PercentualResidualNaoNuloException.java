package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.dadosdepreciacao.exception;

public class PercentualResidualNaoNuloException extends RuntimeException {
    public PercentualResidualNaoNuloException(){ super("Percentual residual não pode ser inserido para conta do tipo não depreciável."); }
}
