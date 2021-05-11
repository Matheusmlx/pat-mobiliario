package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.listagem.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Reserva;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.listagem.BuscarReservasInputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

public class BuscarReservasFiltroConverter extends GenericConverter<BuscarReservasInputData, Reserva.Filtro> {

    public Reserva.Filtro to(BuscarReservasInputData source) {
        Reserva.Filtro target = super.to(source);
        target.setPage(target.getPage() - 1);
        return target;
    }
}
