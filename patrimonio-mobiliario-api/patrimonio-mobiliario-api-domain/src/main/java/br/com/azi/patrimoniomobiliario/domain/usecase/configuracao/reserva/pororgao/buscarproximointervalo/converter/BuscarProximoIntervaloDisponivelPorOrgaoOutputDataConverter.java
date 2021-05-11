package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.buscarproximointervalo.converter;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.buscarproximointervalo.BuscarProximoIntervaloDisponivelPorOrgaoOutputData;

public class BuscarProximoIntervaloDisponivelPorOrgaoOutputDataConverter {
    public BuscarProximoIntervaloDisponivelPorOrgaoOutputData to(Long numeroInicio, Long numeroFim){
        BuscarProximoIntervaloDisponivelPorOrgaoOutputData target = new BuscarProximoIntervaloDisponivelPorOrgaoOutputData();
        target.setNumeroInicio(numeroInicio);
        target.setNumeroFim(numeroFim);
        return target;
    }
}
