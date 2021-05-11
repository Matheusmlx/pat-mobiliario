package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.buscarlistagem.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Concedente;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.buscarlistagem.BuscarConcedentesInputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BuscarConcedentesFiltroConverter extends GenericConverter<BuscarConcedentesInputData, Concedente.Filtro> {

    @Override
    public Concedente.Filtro to(BuscarConcedentesInputData from) {
        Concedente.Filtro target = super.to(from);
        target.setPage(target.getPage() - 1);
        return target;
    }

}
