package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.buscarintervalodisponivel.converter;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.buscarintervalodisponivel.BuscarIntervaloDisponivelOutputData;

public class BuscarIntervaloDisponivelOutputDataConverter {
    public BuscarIntervaloDisponivelOutputData to(Long numeroInicio, Long numeroFim) {
        BuscarIntervaloDisponivelOutputData target = new BuscarIntervaloDisponivelOutputData();
        target.setNumeroInicio(numeroInicio);
        target.setNumeroFim(numeroFim);
        return target;
    }
}
