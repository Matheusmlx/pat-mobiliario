package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.existeporsituacao.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.ReservaIntervalo;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.existeporsituacao.ExisteIntervalosPorSituacaoOutputData;

public class ExisteIntervalosPorSituacaoOutputDataConverter {

    public ExisteIntervalosPorSituacaoOutputData to(Long reservaId, ReservaIntervalo.Situacao situacao, Boolean existeIntervalosPorSituacao) {
        return ExisteIntervalosPorSituacaoOutputData.builder()
            .reservaId(reservaId)
            .intervaloSituacao(situacao.name())
            .existe(existeIntervalosPorSituacao)
            .build();
    }
}
