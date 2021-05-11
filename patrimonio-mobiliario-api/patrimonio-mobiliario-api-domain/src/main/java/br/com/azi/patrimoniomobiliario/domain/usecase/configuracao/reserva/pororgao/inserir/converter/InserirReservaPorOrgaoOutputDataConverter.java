package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.inserir.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Reserva;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.inserir.InserirReservaPorOrgaoOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

import java.util.Objects;

public class InserirReservaPorOrgaoOutputDataConverter extends GenericConverter<Reserva, InserirReservaPorOrgaoOutputData> {

    public InserirReservaPorOrgaoOutputData to(Reserva source) {
        InserirReservaPorOrgaoOutputData target = super.to(source);

        if (Objects.nonNull(source.getPreenchimento())) {
            target.setPreenchimento(source.getPreenchimento().name());
        }

        return target;
    }

}
