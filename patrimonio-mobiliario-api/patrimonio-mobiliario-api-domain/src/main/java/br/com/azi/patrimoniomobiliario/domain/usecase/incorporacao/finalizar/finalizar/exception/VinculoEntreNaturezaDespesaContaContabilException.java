package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizar.exception;

public class VinculoEntreNaturezaDespesaContaContabilException extends RuntimeException{
    public VinculoEntreNaturezaDespesaContaContabilException() { super("A natureza de despesa não está mais associado com a conta contábil.");}
}
