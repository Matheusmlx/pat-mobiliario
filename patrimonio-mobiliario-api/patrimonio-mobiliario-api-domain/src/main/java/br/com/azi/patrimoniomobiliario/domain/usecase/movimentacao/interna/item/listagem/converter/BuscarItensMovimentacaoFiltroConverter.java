package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.listagem.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.MovimentacaoItem;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.listagem.BuscarItensMovimentacaoInputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

public class BuscarItensMovimentacaoFiltroConverter extends GenericConverter<BuscarItensMovimentacaoInputData, MovimentacaoItem.Filtro> {

    @Override
    public MovimentacaoItem.Filtro to(BuscarItensMovimentacaoInputData from) {
        MovimentacaoItem.Filtro target = super.to(from);
        target.setMovimentacaoId(from.getMovimentacaoId());
        target.setPage(target.getPage()-1);
        return target;
    }
}
