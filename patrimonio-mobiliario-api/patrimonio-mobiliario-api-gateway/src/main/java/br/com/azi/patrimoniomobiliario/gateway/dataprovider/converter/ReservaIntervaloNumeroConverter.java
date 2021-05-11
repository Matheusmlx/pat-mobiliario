package br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.ReservaIntervalo;
import br.com.azi.patrimoniomobiliario.domain.entity.ReservaIntervaloNumero;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ReservaIntervaloEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ReservaIntervaloNumeroEntity;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ReservaIntervaloNumeroConverter extends GenericConverter<ReservaIntervaloNumeroEntity, ReservaIntervaloNumero> {

    public ReservaIntervaloNumero to(ReservaIntervaloNumeroEntity source) {
        ReservaIntervaloNumero target = super.to(source);

        if(Objects.nonNull(source.getReservaIntervalo())) {
            target.setReservaIntervalo(
                ReservaIntervalo
                    .builder()
                    .id(source.getReservaIntervalo().getId())
                    .build()
            );
        }

        return target;
    }

    public ReservaIntervaloNumeroEntity from(ReservaIntervaloNumero source) {
        ReservaIntervaloNumeroEntity target = super.from(source);

        if(Objects.nonNull(source.getReservaIntervalo())) {
            target.setReservaIntervalo(
                ReservaIntervaloEntity
                    .builder()
                    .id(source.getReservaIntervalo().getId())
                    .build()
            );
        }

        return target;
    }

}
