package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.cadastro.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Convenio;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.cadastro.CadastrarConvenioOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

import java.util.Objects;

public class CadastrarConvenioOutputDataConverter extends GenericConverter<Convenio, CadastrarConvenioOutputData> {
    @Override
    public CadastrarConvenioOutputData to(Convenio source) {
        CadastrarConvenioOutputData target = super.to(source);

        if (Objects.nonNull(source.getId())) {
            target.setId(source.getId());
        }

        if (Objects.nonNull(source.getNome())) {
            target.setNome(source.getNome());
        }

        if (Objects.nonNull(source.getNumero())) {
            target.setNumero(source.getNumero());
        }

        if (Objects.nonNull(source.getFonteRecurso())) {
            target.setFonteRecurso(source.getFonteRecurso());
        }

        if (Objects.nonNull(source.getSituacao())) {
            target.setSituacao(source.getSituacao().toString());
        }

        if (Objects.nonNull(source.getConcedente())) {
            target.setConcedente(source.getConcedente().getId());
        }
        return target;
    }
}
