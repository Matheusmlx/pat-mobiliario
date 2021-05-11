package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.finalizar.exception;

public class ReservaNaoPossuiIntervalosEmElaboracaoException extends RuntimeException {
    public ReservaNaoPossuiIntervalosEmElaboracaoException() {
        super("A reserva não possui intervalos em elaboração cadastrados");
    }
}
