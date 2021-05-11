package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.finalizacao.exception;

public class SetorNaoAlmoxarifadoException extends RuntimeException {

    public SetorNaoAlmoxarifadoException(String mensagem) {
        super(mensagem);
    }

}
