package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.dadosdepreciacao.exception;

public class VidaUtilNulaException extends RuntimeException{
    public VidaUtilNulaException(){ super("Vida útil não pode ser nula para conta do tipo depreciável."); }
}
