package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.visualizar.exception;

public class ItemIncorporacaoNaoEncontradaException extends RuntimeException {
    public ItemIncorporacaoNaoEncontradaException() {
        super("Item não encontrado.");
    }
}
