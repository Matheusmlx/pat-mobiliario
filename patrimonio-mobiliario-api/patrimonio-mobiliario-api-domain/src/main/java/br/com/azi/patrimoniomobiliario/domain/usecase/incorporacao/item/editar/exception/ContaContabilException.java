package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.editar.exception;

public class ContaContabilException extends RuntimeException {
    public ContaContabilException(){ super("Não foi possível vincular conta contábil!"); }
}
