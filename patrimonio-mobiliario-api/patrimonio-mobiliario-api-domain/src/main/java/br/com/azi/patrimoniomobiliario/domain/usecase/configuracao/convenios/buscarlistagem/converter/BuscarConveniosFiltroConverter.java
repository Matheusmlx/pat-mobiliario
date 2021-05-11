package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.buscarlistagem.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Convenio;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.buscarlistagem.BuscarConveniosInputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BuscarConveniosFiltroConverter extends GenericConverter<BuscarConveniosInputData, Convenio.Filtro> {

    @Override
    public Convenio.Filtro to(BuscarConveniosInputData from) {
        Convenio.Filtro target = super.to(from);
        target.setPage(target.getPage() - 1);
        return target;
    }

}
