package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.devolucaoalmoxarifado.finalizacao.exception;

public class SetorInativoException extends RuntimeException {

    public SetorInativoException(String mensagem) {
        super(mensagem);
    }

}
