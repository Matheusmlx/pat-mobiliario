package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.edicao.exception;

public class PreenchimentoInvalidoException extends RuntimeException {
    public PreenchimentoInvalidoException() {
        super("O tipo do preenchimento é inválido");
    }
}
