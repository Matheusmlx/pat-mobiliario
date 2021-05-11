package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.devolucaoalmoxarifado.finalizacao.exception;

public class SetorNaoAlmoxarifadoException extends RuntimeException {

    public SetorNaoAlmoxarifadoException() {
        super("Setor destino não é almoxarifado!");
    }

}
