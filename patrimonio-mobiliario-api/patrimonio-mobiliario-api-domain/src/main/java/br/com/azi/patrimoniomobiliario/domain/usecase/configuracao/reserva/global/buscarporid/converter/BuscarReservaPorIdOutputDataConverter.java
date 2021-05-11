package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.buscarporid.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Reserva;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.buscarporid.BuscarReservaPorIdOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

import java.util.Objects;

public class BuscarReservaPorIdOutputDataConverter extends GenericConverter<Reserva, BuscarReservaPorIdOutputData> {

    @Override
    public BuscarReservaPorIdOutputData to(Reserva source) {
        BuscarReservaPorIdOutputData target = super.to(source);

        if (Objects.nonNull(source.getPreenchimento())) {
            target.setPreenchimento(source.getPreenchimento().name());
        }

        return target;
    }
}
