package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.listagem.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.listagem.BuscarMovimentacoesInternasInputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

public class BuscarMovimentacoesInternasFiltroConverter extends GenericConverter<BuscarMovimentacoesInternasInputData, Movimentacao.Filtro> {

    @Override
    public Movimentacao.Filtro to(BuscarMovimentacoesInternasInputData from) {
        Movimentacao.Filtro target = super.to(from);
        target.setPage(target.getPage()-1);
        return target;
    }

}
