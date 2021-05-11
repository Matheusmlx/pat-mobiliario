package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.edicao.exception;

public class DataInicioMaiorDataFimException extends RuntimeException{
    public DataInicioMaiorDataFimException() { super("Data Inicial maior que data final do convênio!"); }
}
