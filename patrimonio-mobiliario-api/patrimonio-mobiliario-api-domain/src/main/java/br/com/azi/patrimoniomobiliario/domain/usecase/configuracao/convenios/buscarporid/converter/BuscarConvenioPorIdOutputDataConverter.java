package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.buscarporid.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Convenio;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.buscarporid.BuscarConvenioPorIdOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

import java.util.Objects;

public class BuscarConvenioPorIdOutputDataConverter extends GenericConverter<Convenio, BuscarConvenioPorIdOutputData> {

    @Override
    public BuscarConvenioPorIdOutputData to(Convenio source) {
        BuscarConvenioPorIdOutputData target = super.to(source);

        if (Objects.nonNull(source.getConcedente())) {
            target.setConcedente(source.getConcedente().getId());
        }
        return target;
    }
}
