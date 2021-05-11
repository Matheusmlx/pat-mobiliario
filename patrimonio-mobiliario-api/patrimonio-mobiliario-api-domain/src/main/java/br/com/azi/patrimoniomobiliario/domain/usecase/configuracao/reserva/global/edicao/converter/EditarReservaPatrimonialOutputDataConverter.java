package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.edicao.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Reserva;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.edicao.EditarReservaPatrimonialOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

import java.util.Objects;

public class EditarReservaPatrimonialOutputDataConverter extends GenericConverter<Reserva, EditarReservaPatrimonialOutputData> {

    @Override
    public EditarReservaPatrimonialOutputData to(Reserva source) {
        EditarReservaPatrimonialOutputData target = super.to(source);

        if (Objects.nonNull(source.getPreenchimento())) {
            target.setPreenchimento(source.getPreenchimento().name());
        }

        return target;
    }
}
