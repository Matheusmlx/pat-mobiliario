package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemporincorporacao.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemporincorporacao.BuscarPatrimoniosIncorporacaoInputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

public class BuscarPatrimoniosIncorporacaoFiltroConverter extends GenericConverter<BuscarPatrimoniosIncorporacaoInputData, Patrimonio.Filtro> {

    @Override
    public Patrimonio.Filtro to(BuscarPatrimoniosIncorporacaoInputData source) {
        Patrimonio.Filtro target = super.to(source);
        target.setPage(target.getPage()-1);
        return target;
    }
}
