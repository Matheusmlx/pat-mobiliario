package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entresetores.edicao.exception;

public class SetorOrigemEDestinoSaoIguaisException extends RuntimeException {
    public SetorOrigemEDestinoSaoIguaisException() {
        super("Setor origem e destino não podem ser iguais.");
    }
}
