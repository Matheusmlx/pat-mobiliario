package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.dadosdepreciacao.exception;

public class VidaUtilNaoNulaException extends RuntimeException{
    public VidaUtilNaoNulaException(){ super("Vida útil não pode ser inserida para conta do tipo não depreciável."); }
}
