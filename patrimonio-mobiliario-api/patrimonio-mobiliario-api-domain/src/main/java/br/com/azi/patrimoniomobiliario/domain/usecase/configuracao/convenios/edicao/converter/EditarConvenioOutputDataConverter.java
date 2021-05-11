package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.edicao.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Convenio;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.edicao.EditarConvenioOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

import java.util.Objects;

public class EditarConvenioOutputDataConverter extends GenericConverter<Convenio, EditarConvenioOutputData> {
    @Override
    public EditarConvenioOutputData to(Convenio source) {
        EditarConvenioOutputData target = super.to(source);

        if (Objects.nonNull(source.getConcedente())) {
            target.setConcedente(source.getConcedente().getId());
        }
        return target;
    }
}
