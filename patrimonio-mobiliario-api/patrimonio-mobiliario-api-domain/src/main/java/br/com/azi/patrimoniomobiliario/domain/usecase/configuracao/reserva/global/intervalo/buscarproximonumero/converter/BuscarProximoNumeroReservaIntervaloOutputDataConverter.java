package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.buscarproximonumero.converter;


import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.buscarproximonumero.BuscarProximoNumeroReservaIntervaloOutputData;

public class BuscarProximoNumeroReservaIntervaloOutputDataConverter {

    public BuscarProximoNumeroReservaIntervaloOutputData to(Long source) {
        BuscarProximoNumeroReservaIntervaloOutputData target = new BuscarProximoNumeroReservaIntervaloOutputData();
        target.setProximoNumero(source);
        return target;
    }

}
