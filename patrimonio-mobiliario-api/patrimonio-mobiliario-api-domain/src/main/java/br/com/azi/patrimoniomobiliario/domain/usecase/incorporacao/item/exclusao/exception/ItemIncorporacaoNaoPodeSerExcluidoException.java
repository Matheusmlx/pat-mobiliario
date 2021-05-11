package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.exclusao.exception;

public class ItemIncorporacaoNaoPodeSerExcluidoException extends RuntimeException {
    public ItemIncorporacaoNaoPodeSerExcluidoException() {
        super("Não é possível remover um item de uma incorporação finalizada.");
    }
}
