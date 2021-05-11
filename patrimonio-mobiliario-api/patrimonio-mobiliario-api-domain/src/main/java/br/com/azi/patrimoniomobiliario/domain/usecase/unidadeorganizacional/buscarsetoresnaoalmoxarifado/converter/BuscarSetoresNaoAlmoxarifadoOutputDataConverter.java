package br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarsetoresnaoalmoxarifado.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarsetoresnaoalmoxarifado.BuscarSetoresNaoAlmoxarifadoOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

import java.util.List;
import java.util.stream.Collectors;

public class BuscarSetoresNaoAlmoxarifadoOutputDataConverter extends GenericConverter<UnidadeOrganizacional, BuscarSetoresNaoAlmoxarifadoOutputData> {

    private static final BuscarSetoresNaoAlmoxarifadoOutputDataConverter.ItemOutputDataConverter ITEM_OUTPUT_DATA_CONVERTER =
        new BuscarSetoresNaoAlmoxarifadoOutputDataConverter.ItemOutputDataConverter();

    public BuscarSetoresNaoAlmoxarifadoOutputData to(List<UnidadeOrganizacional> source) {
        List<BuscarSetoresNaoAlmoxarifadoOutputData.UnidadeOrganizacional> itens = source.stream()
            .map(this::converterItem)
            .collect(Collectors.toList());

        BuscarSetoresNaoAlmoxarifadoOutputData target = new BuscarSetoresNaoAlmoxarifadoOutputData();
        target.setItems(itens);

        return target;
    }

    private BuscarSetoresNaoAlmoxarifadoOutputData.UnidadeOrganizacional converterItem(UnidadeOrganizacional source) {
        return ITEM_OUTPUT_DATA_CONVERTER.to(source);
    }

    private static class ItemOutputDataConverter extends GenericConverter<UnidadeOrganizacional, BuscarSetoresNaoAlmoxarifadoOutputData.UnidadeOrganizacional> {
    }

}
