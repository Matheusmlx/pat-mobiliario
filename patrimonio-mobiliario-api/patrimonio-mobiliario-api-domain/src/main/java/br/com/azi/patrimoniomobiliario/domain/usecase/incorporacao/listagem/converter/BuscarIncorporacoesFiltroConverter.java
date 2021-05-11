package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.listagem.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.listagem.BuscarIncorporacoesInputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

public class BuscarIncorporacoesFiltroConverter extends GenericConverter<BuscarIncorporacoesInputData, Incorporacao.Filtro> {

    @Override
    public Incorporacao.Filtro to(BuscarIncorporacoesInputData from){
        Incorporacao.Filtro target = super.to(from);
        target.setPage(target.getPage() - 1);
        return target;
    }
}
