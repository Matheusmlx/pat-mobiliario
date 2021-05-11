package br.com.azi.patrimoniomobiliario.domain.usecase.comodante.listagem.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Comodante;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.usecase.comodante.listagem.BuscarComodantesOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

import java.util.stream.Collectors;

public class BuscarComodantesOutputDataConverter {

    public BuscarComodantesOutputData to(ListaPaginada<Comodante> source) {
        BuscarComodanteOutputDataConverter outputDataConverter = new BuscarComodanteOutputDataConverter();

        return BuscarComodantesOutputData
            .builder()
            .items(source.getItems()
                .stream()
                .map(outputDataConverter::to)
                .collect(Collectors.toList()))
            .totalElements(source.getTotalElements())
            .build();
    }

    public static class BuscarComodanteOutputDataConverter extends GenericConverter<Comodante, BuscarComodantesOutputData.Item> {

    }

}
