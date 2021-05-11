package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.editar.exception;

public class ConfigContaContabilException extends RuntimeException{

    public ConfigContaContabilException() {
        super("Configure o tipo de conta contábil antes de usá-la em uma incorporação.");
    }
}
