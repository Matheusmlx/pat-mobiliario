package br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarfundos.converter;


import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarfundos.BuscarFundosOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

import java.util.List;
import java.util.stream.Collectors;


public class BuscarFundosOutputDataConverter {

    private static final ItemOutputDataConverter ITEM_OUTPUT_DATA_CONVERTER = new ItemOutputDataConverter();

    public BuscarFundosOutputData to(List<UnidadeOrganizacional> source) {
        List<BuscarFundosOutputData.UnidadeOrganizacional> itens = source.stream()
            .map(this::converterItem)
            .collect(Collectors.toList());

        BuscarFundosOutputData target = new BuscarFundosOutputData();
        target.setItems(itens);

        return target;
    }

    private BuscarFundosOutputData.UnidadeOrganizacional converterItem(UnidadeOrganizacional source) {
        return ITEM_OUTPUT_DATA_CONVERTER.to(source);
    }

    private static class ItemOutputDataConverter extends GenericConverter<UnidadeOrganizacional, BuscarFundosOutputData.UnidadeOrganizacional> {
    }

}
