package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entresetores.finalizacao.exception;

public class SetorTipoAlmoxarifadoException extends RuntimeException {
    public SetorTipoAlmoxarifadoException(String message) {
        super(message);
    }

    public SetorTipoAlmoxarifadoException() {
        super("O setor é tipo almoxarifado!");
    }
}
