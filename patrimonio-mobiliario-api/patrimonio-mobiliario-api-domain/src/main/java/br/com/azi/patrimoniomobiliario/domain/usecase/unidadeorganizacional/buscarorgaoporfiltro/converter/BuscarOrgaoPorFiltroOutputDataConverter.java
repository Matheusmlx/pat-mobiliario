package br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarorgaoporfiltro.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarorgaoporfiltro.BuscarOrgaoPorFiltroOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import lombok.AllArgsConstructor;

import java.util.stream.Collectors;

public class BuscarOrgaoPorFiltroOutputDataConverter {
    public BuscarOrgaoPorFiltroOutputData to(ListaPaginada<UnidadeOrganizacional> source) {
        BuscarOrgaoPorFiltroItemConverter itemConverter = new BuscarOrgaoPorFiltroItemConverter();

        return BuscarOrgaoPorFiltroOutputData.builder()
            .totalElements(source.getTotalElements())
            .totalPages(source.getTotalPages())
            .items(source
                .getItems()
                .stream()
                .map(itemConverter::to)
                .collect(Collectors.toList())
            )
            .build();
    }

    @AllArgsConstructor
    public static class BuscarOrgaoPorFiltroItemConverter extends GenericConverter<UnidadeOrganizacional, BuscarOrgaoPorFiltroOutputData.UnidadeOrganizacional> {

    }
}
