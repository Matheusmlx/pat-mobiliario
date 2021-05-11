package br.com.azi.patrimoniomobiliario.domain.usecase.responsavel.listagem.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Responsavel;
import br.com.azi.patrimoniomobiliario.domain.usecase.responsavel.listagem.BuscarResponsaveisOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

import java.util.List;
import java.util.stream.Collectors;

public class BuscarResponsaveisOutputDataConverter {

    public BuscarResponsaveisOutputData to(List<Responsavel> responsaveis) {
        BuscarResponsavelOutputDataConverter converter = new BuscarResponsavelOutputDataConverter();

        return BuscarResponsaveisOutputData
            .builder()
            .items(
                responsaveis
                .stream()
                .map(converter::to)
                .collect(Collectors.toList()))
            .build();
    }

    public static class BuscarResponsavelOutputDataConverter extends GenericConverter<Responsavel, BuscarResponsaveisOutputData.Item> {
    }

}
