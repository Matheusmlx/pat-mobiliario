package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.editar.exception;

public class ItemComMesmaNaturezaException extends RuntimeException {
    public ItemComMesmaNaturezaException(){ super("As mesmas informações já existem para outro item na incorporação."); }
}
