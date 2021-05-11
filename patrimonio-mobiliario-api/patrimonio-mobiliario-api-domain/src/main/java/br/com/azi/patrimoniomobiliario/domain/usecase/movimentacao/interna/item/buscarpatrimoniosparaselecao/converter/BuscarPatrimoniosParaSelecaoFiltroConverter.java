package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.buscarpatrimoniosparaselecao.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.buscarpatrimoniosparaselecao.BuscarPatrimoniosParaSelecaoInputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

public class BuscarPatrimoniosParaSelecaoFiltroConverter extends GenericConverter<BuscarPatrimoniosParaSelecaoInputData, Patrimonio.Filtro> {

    @Override
    public Patrimonio.Filtro to(BuscarPatrimoniosParaSelecaoInputData source) {
        Patrimonio.Filtro target = super.to(source);
        target.setPage(source.getPage()-1);
        return target;
    }

}
