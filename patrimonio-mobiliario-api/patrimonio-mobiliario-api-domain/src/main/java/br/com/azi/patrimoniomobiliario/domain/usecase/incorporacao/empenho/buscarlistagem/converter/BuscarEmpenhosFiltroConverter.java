package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.buscarlistagem.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Empenho;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.buscarlistagem.BuscarEmpenhosInputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

public class BuscarEmpenhosFiltroConverter extends GenericConverter<BuscarEmpenhosInputData, Empenho.Filtro> {

    @Override
    public Empenho.Filtro to(BuscarEmpenhosInputData from) {
        Empenho.Filtro target = super.to(from);
        target.setIncorporacaoId(from.getIncorporacaoId());
        return target;
    }

}

