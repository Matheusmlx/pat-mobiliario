package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.devolucaoalmoxarifado.finalizacao.exception;

public class SetorAlmoxarifadoException extends RuntimeException {

    public SetorAlmoxarifadoException() {
        super("Setor origem é almoxarifado!");
    }

}
