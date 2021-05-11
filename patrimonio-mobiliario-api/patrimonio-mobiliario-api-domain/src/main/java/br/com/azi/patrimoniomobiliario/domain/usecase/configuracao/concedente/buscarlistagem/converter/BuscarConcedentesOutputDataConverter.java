package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.buscarlistagem.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Concedente;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.buscarlistagem.BuscarConcedentesOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

import java.util.stream.Collectors;

public class BuscarConcedentesOutputDataConverter {

    public BuscarConcedentesOutputData to(ListaPaginada<Concedente> from) {
        BuscarConcedentesOutputDataItemConverter outputDataItemConverter = new BuscarConcedentesOutputDataItemConverter();

        return BuscarConcedentesOutputData
            .builder()
            .totalElements(from.getTotalElements())
            .totalPages(from.getTotalPages())
            .items(from
                .getItems()
                .stream()
                .map(outputDataItemConverter::to)
                .collect(Collectors.toList()))
            .build();
    }

    private static class BuscarConcedentesOutputDataItemConverter extends GenericConverter<Concedente, BuscarConcedentesOutputData.Item> { }
}
