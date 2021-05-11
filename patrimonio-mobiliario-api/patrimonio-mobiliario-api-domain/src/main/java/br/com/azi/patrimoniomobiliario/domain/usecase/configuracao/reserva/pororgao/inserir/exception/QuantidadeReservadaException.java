package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.inserir.exception;

public class QuantidadeReservadaException extends RuntimeException {
    public QuantidadeReservadaException(String mensagem) {
        super(mensagem);
    }
}
