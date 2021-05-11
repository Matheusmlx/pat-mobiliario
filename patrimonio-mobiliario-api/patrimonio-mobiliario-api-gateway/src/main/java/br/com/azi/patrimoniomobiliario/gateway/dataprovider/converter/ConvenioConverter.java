package br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Convenio;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ConcedenteEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ConvenioEntity;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import br.com.azi.patrimoniomobiliario.utils.date.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ConvenioConverter  extends GenericConverter<ConvenioEntity, Convenio> {

    @Autowired
    ConcedenteConverter concedenteConverter;

    @Override
    public ConvenioEntity from(Convenio source) {
        ConvenioEntity target = super.from(source);

        if (Objects.nonNull(source.getDataVigenciaInicio())) {
            target.setDataVigenciaInicio(DateUtils.asDate(source.getDataVigenciaInicio()));
        }

        if (Objects.nonNull(source.getDataVigenciaFim())) {
            target.setDataVigenciaFim(DateUtils.asDate(source.getDataVigenciaFim()));
        }

        if (Objects.nonNull(source.getSituacao())) {
            target.setSituacao(source.getSituacao().toString());
        }

        if (Objects.nonNull(source.getConcedente()) && Objects.nonNull(source.getConcedente().getId())) {
            target.setConcedente(
                ConcedenteEntity
                    .builder()
                    .id(source.getConcedente().getId())
                    .nome(source.getConcedente().getNome())
                    .build()
            );
        }
        return target;
    }

    @Override
    public Convenio to(ConvenioEntity source) {
        Convenio target = super.to(source);

        if (Objects.nonNull(source.getDataVigenciaInicio())) {
            target.setDataVigenciaInicio(DateUtils.asLocalDateTime(source.getDataVigenciaInicio()));
        }

        if (Objects.nonNull(source.getDataVigenciaFim())) {
            target.setDataVigenciaFim(DateUtils.asLocalDateTime(source.getDataVigenciaFim()));
        }

        if (Objects.nonNull(source.getSituacao())) {
            target.setSituacao(Convenio.Situacao.valueOf(source.getSituacao()));
        }

        if (Objects.nonNull(source.getConcedente())) {
            target.setConcedente(concedenteConverter.to(source.getConcedente()));
        }
        return target;
    }
}

