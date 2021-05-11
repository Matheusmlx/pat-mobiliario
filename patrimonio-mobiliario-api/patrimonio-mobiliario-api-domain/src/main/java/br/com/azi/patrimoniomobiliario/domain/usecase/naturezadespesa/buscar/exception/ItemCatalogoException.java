package br.com.azi.patrimoniomobiliario.domain.usecase.naturezadespesa.buscar.exception;

public class ItemCatalogoException extends RuntimeException{
    public ItemCatalogoException() { super("Não foi possível encontar o item catálogo!"); }
}
