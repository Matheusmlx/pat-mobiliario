package br.com.azi.patrimoniomobiliario.domain.usecase.naturezadespesa.buscar.conveter;

import br.com.azi.patrimoniomobiliario.domain.entity.NaturezaDespesa;
import br.com.azi.patrimoniomobiliario.domain.usecase.naturezadespesa.buscar.BuscarNaturezasDespesaOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

import java.util.List;
import java.util.stream.Collectors;

public class BuscarNaturezasDespesaOutputDataConverter {



    private static final ItemOutputDataConverter ITEM_OUTPUT_DATA_CONVERTER = new ItemOutputDataConverter();

    public BuscarNaturezasDespesaOutputData to(List<NaturezaDespesa> source) {
        List<BuscarNaturezasDespesaOutputData.NaturezaDespesa> itens = source.stream()
            .map(this::converterItem)
            .collect(Collectors.toList());

        BuscarNaturezasDespesaOutputData target = new BuscarNaturezasDespesaOutputData();
        target.setItems(itens);

        return target;
    }

    private BuscarNaturezasDespesaOutputData.NaturezaDespesa converterItem(NaturezaDespesa source) {
        return ITEM_OUTPUT_DATA_CONVERTER.to(source);
    }

    private static class ItemOutputDataConverter extends GenericConverter<NaturezaDespesa, BuscarNaturezasDespesaOutputData.NaturezaDespesa> {
    }
}
