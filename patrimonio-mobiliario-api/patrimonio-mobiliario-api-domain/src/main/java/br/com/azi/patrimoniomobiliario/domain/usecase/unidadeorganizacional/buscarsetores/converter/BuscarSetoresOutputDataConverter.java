package br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarsetores.converter;


import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarsetores.BuscarSetoresOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

import java.util.List;
import java.util.stream.Collectors;


public class BuscarSetoresOutputDataConverter {

    private static final ItemOutputDataConverter ITEM_OUTPUT_DATA_CONVERTER = new ItemOutputDataConverter();

    public BuscarSetoresOutputData to(List<UnidadeOrganizacional> source) {
        List<BuscarSetoresOutputData.UnidadeOrganizacional> itens = source.stream()
            .map(this::converterItem)
            .collect(Collectors.toList());

        BuscarSetoresOutputData target = new BuscarSetoresOutputData();
        target.setItems(itens);

        return target;
    }

    private BuscarSetoresOutputData.UnidadeOrganizacional converterItem(UnidadeOrganizacional source) {
        return ITEM_OUTPUT_DATA_CONVERTER.to(source);
    }

    private static class ItemOutputDataConverter extends GenericConverter<UnidadeOrganizacional, BuscarSetoresOutputData.UnidadeOrganizacional> {
    }
}
