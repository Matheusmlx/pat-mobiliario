package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.buscarproximonumero.converter;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.buscarproximonumero.BuscarProximoNumeroReservaPorOrgaoOutputData;

public class BuscarProximoNumeroReservaPorOrgaoOutputDataConverter {

    public BuscarProximoNumeroReservaPorOrgaoOutputData to(Long source) {
        BuscarProximoNumeroReservaPorOrgaoOutputData target = new BuscarProximoNumeroReservaPorOrgaoOutputData();
        target.setProximoNumero(source);
        return target;
    }

}
