package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.envio.exception;

public class SetorInativoException extends RuntimeException {

    public SetorInativoException(String mensagem) {
        super(mensagem);
    }

}
