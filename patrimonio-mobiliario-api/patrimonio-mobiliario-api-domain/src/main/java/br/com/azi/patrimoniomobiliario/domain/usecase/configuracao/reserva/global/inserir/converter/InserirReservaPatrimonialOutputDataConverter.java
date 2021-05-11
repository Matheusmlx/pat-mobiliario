package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.inserir.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Reserva;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.inserir.InserirReservaPatrimonialOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

import java.util.Objects;

public class InserirReservaPatrimonialOutputDataConverter extends GenericConverter<Reserva, InserirReservaPatrimonialOutputData> {

    @Override
    public InserirReservaPatrimonialOutputData to(Reserva source) {
        InserirReservaPatrimonialOutputData target = super.to(source);

        if (Objects.nonNull(source.getPreenchimento())) {
            target.setPreenchimento(source.getPreenchimento().name());
        }

        return target;
    }

}
