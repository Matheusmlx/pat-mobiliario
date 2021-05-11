package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemincorporados.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemincorporados.BuscarPatrimoniosIncorporadosInputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

public class BuscarPatrimoniosIncorporadosFiltroConverter extends GenericConverter<BuscarPatrimoniosIncorporadosInputData, Patrimonio.Filtro> {

    public Patrimonio.Filtro to(BuscarPatrimoniosIncorporadosInputData source) {
        Patrimonio.Filtro target = super.to(source);
        target.setPage(target.getPage()-1);
        return target;
    }
}
