package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.buscarporid.exception;

public class ItemNaoPertenceAIncorporacaoException extends RuntimeException {

    public ItemNaoPertenceAIncorporacaoException() { super("Item não existe na incorporação!"); }
}
