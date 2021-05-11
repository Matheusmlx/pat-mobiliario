package br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Empenho;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.EmpenhoEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.IncorporacaoEntity;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import br.com.azi.patrimoniomobiliario.utils.date.DateUtils;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class EmpenhoConverter extends GenericConverter<EmpenhoEntity, Empenho> {

    @Override
    public Empenho to(EmpenhoEntity source) {
        Empenho target = super.to(source);

        if(Objects.nonNull(source.getDataEmpenho())) {
            target.setDataEmpenho(DateUtils.asLocalDateTime(source.getDataEmpenho()));
        }
        if(Objects.nonNull(source.getIncorporacao())) {
            target.setIncorporacaoId(source.getIncorporacao().getId());
        }

        return target;
    }

    @Override
    public EmpenhoEntity from(Empenho source) {
        EmpenhoEntity target = super.from(source);

        if(Objects.nonNull(source.getDataEmpenho())) {
            target.setDataEmpenho(DateUtils.asDate(source.getDataEmpenho()));
        }
        if(Objects.nonNull(source.getIncorporacaoId())) {
            target.setIncorporacao(
                IncorporacaoEntity
                    .builder()
                    .id(source.getIncorporacaoId())
                    .build()
            );
        }

        return target;
    }
}
