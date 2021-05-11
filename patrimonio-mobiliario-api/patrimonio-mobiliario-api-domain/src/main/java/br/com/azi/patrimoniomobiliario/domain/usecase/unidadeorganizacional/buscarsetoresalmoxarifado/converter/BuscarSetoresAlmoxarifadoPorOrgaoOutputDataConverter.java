package br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarsetoresalmoxarifado.converter;



import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarsetoresalmoxarifado.BuscarSetoresAlmoxarifadoPorOrgaoOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

import java.util.List;
import java.util.stream.Collectors;


public class BuscarSetoresAlmoxarifadoPorOrgaoOutputDataConverter {

    private static final ItemOutputDataConverter ITEM_OUTPUT_DATA_CONVERTER = new ItemOutputDataConverter();

    public BuscarSetoresAlmoxarifadoPorOrgaoOutputData to(List<UnidadeOrganizacional> source) {
        List<BuscarSetoresAlmoxarifadoPorOrgaoOutputData.UnidadeOrganizacional> itens = source.stream()
            .map(this::converterItem)
            .collect(Collectors.toList());

        BuscarSetoresAlmoxarifadoPorOrgaoOutputData target = new BuscarSetoresAlmoxarifadoPorOrgaoOutputData();
        target.setItems(itens);

        return target;
    }

    private BuscarSetoresAlmoxarifadoPorOrgaoOutputData.UnidadeOrganizacional converterItem(UnidadeOrganizacional source) {
        return ITEM_OUTPUT_DATA_CONVERTER.to(source);
    }

    private static class ItemOutputDataConverter extends GenericConverter<UnidadeOrganizacional, BuscarSetoresAlmoxarifadoPorOrgaoOutputData.UnidadeOrganizacional> {
    }
}
