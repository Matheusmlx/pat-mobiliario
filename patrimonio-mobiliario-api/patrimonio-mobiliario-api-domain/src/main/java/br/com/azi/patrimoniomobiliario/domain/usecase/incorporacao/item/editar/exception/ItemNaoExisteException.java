package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.editar.exception;

public class ItemNaoExisteException extends RuntimeException{
    public ItemNaoExisteException(){super("Este item não existe!");}
}
