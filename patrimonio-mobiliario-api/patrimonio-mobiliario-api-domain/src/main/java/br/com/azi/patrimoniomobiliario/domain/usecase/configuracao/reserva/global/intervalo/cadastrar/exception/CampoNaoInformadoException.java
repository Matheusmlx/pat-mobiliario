package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.cadastrar.exception;

public class CampoNaoInformadoException extends RuntimeException {
    public CampoNaoInformadoException(String mensagem) {
        super(mensagem);
    }
}
