package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagem.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagem.BuscarPatrimoniosPorItemIncorporacaoInputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

public class BuscarPatrimoniosPorItemIncorporacaoFiltroConverter extends GenericConverter<BuscarPatrimoniosPorItemIncorporacaoInputData, Patrimonio.Filtro> {

    @Override
    public Patrimonio.Filtro to(BuscarPatrimoniosPorItemIncorporacaoInputData from) {
        Patrimonio.Filtro target = super.to(from);
        target.setItemIncorporacaoId(from.getItemIncorporacaoId());
        target.setPage(target.getPage()-1);
        return target;
    }

}
