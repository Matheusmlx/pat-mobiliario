package br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Reserva;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ReservaEntity;
import br.com.azi.patrimoniomobiliario.utils.date.DateUtils;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ReservaConverter extends GenericConverter<ReservaEntity, Reserva> {

    @Override
    public Reserva to(ReservaEntity source) {
        Reserva target = super.to(source);

        if (Objects.nonNull(source.getDataCriacao())) {
            target.setDataCriacao(DateUtils.asLocalDateTime(source.getDataCriacao()));
        }

        return target;
    }

    @Override
    public ReservaEntity from(Reserva source) {
        ReservaEntity target = super.from(source);

        if (Objects.nonNull(source.getDataCriacao())) {
            target.setDataCriacao(DateUtils.asDate(source.getDataCriacao()));
        }
        
        return target;
    }
}
