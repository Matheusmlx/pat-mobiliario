package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.listagem.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.ReservaIntervalo;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.listagem.BuscarReservaIntervalosInputData;

public class BuscarReservaIntervalosFiltroConverter {

    public ReservaIntervalo.Filtro to(BuscarReservaIntervalosInputData source) {
        ReservaIntervalo.Filtro target = new ReservaIntervalo.Filtro();
        target.setReservaId(source.getReservaId());
        target.setPage(source.getPage() - 1);
        target.setSize(source.getSize());
        return target;
    }

}
