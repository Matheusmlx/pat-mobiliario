package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.editar.exception;

public class ItemNaoEncontradoException extends RuntimeException{
    public ItemNaoEncontradoException(){super("Item não encontrado!");}
}
