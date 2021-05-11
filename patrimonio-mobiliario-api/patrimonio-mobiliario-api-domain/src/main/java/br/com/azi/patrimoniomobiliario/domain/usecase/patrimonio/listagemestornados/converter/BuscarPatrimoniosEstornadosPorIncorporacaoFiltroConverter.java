package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemestornados.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemestornados.BuscarPatrimoniosEstornadosPorIncorporacaoInputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

public class BuscarPatrimoniosEstornadosPorIncorporacaoFiltroConverter extends GenericConverter<BuscarPatrimoniosEstornadosPorIncorporacaoInputData, Patrimonio.Filtro> {

    @Override
    public Patrimonio.Filtro to(BuscarPatrimoniosEstornadosPorIncorporacaoInputData source) {
        Patrimonio.Filtro target = super.to(source);
        target.setPage(source.getPage()-1);
        return target;
    }
}
