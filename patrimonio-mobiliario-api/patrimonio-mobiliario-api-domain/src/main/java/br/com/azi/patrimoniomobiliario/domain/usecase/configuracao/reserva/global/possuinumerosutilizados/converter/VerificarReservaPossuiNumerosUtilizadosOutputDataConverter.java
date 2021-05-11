package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.possuinumerosutilizados.converter;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.possuinumerosutilizados.VerificarReservaPossuiNumerosUtilizadosOutputData;

public class VerificarReservaPossuiNumerosUtilizadosOutputDataConverter {

    public VerificarReservaPossuiNumerosUtilizadosOutputData to(Boolean possuiUtilizados) {
        return VerificarReservaPossuiNumerosUtilizadosOutputData
            .builder()
            .possuiNumerosUtilizados(possuiUtilizados)
            .build();
    }

}
