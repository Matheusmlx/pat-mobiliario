package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.bucarpatrimoniosdevolucao.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.bucarpatrimoniosdevolucao.BuscarPatrimoniosDevolucaoMovimentacaoTemporariaInputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

public class BuscarPatrimoniosDevolucaoMovimentacaoTemporariaFiltroConverter extends GenericConverter<BuscarPatrimoniosDevolucaoMovimentacaoTemporariaInputData, Patrimonio.Filtro> {

    @Override
    public Patrimonio.Filtro to(BuscarPatrimoniosDevolucaoMovimentacaoTemporariaInputData source) {
        Patrimonio.Filtro target = super.to(source);
        target.setPage(source.getPage()-1);
        return target;
    }
}
