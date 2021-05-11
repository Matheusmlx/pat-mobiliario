package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.intervalo.relatorios.termoresponsabilidade.exception;

public class IntervaloNaoPertenceReservaException extends RuntimeException {
    public IntervaloNaoPertenceReservaException() {
        super("Intervalo não pertence a essa reserva");
    }
}
