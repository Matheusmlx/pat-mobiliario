package br.com.azi.patrimoniomobiliario.domain.usecase.comodante.listagem.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Comodante;
import br.com.azi.patrimoniomobiliario.domain.usecase.comodante.listagem.BuscarComodantesInputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

public class BuscarComodantesFiltroConverter extends GenericConverter<BuscarComodantesInputData, Comodante.Filtro> {

    @Override
    public Comodante.Filtro to(BuscarComodantesInputData target) {
        Comodante.Filtro source = super.to(target);
        source.setPage(target.getPage()-1);
        return source;
    }

}
