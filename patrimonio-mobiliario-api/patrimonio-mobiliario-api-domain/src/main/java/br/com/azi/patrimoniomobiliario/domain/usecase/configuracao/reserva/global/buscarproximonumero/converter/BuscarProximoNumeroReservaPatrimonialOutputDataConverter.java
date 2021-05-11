package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.buscarproximonumero.converter;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.buscarproximonumero.BuscarProximoNumeroReservaPatrimonialOutputData;

public class BuscarProximoNumeroReservaPatrimonialOutputDataConverter {

    public BuscarProximoNumeroReservaPatrimonialOutputData to(Long source) {
        BuscarProximoNumeroReservaPatrimonialOutputData target = new BuscarProximoNumeroReservaPatrimonialOutputData();
        target.setProximoNumero(source);
        return target;
    }

}
