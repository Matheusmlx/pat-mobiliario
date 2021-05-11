package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.buscarlistagem.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Empenho;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.buscarlistagem.BuscarEmpenhosOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

import java.util.List;
import java.util.stream.Collectors;

public class BuscarEmpenhosOutputDataConverter {

    public BuscarEmpenhosOutputData to(List<Empenho> from) {
        BuscarEmpenhoOutputDataConverter outputDataConverter = new BuscarEmpenhoOutputDataConverter();

        return BuscarEmpenhosOutputData
            .builder()
            .items(from
                .stream()
                .map(outputDataConverter::to)
                .collect(Collectors.toList()))
            .totalElements((long) from.size())
            .build();
    }

    public static class BuscarEmpenhoOutputDataConverter extends GenericConverter<Empenho, BuscarEmpenhosOutputData.Item> {
    }
}
