package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.listagem.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.ItemIncorporacao;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.listagem.BuscarItensIncorporacaoInputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BuscarItensIncorporacaoFiltroConverter extends GenericConverter<BuscarItensIncorporacaoInputData, ItemIncorporacao.Filtro> {

    @Override
    public ItemIncorporacao.Filtro to(BuscarItensIncorporacaoInputData from) {
        ItemIncorporacao.Filtro target = super.to(from);
        target.setPage(target.getPage() - 1);
        target.setIncorporacaoId(from.getIncorporacaoId());
        return target;
    }

}
