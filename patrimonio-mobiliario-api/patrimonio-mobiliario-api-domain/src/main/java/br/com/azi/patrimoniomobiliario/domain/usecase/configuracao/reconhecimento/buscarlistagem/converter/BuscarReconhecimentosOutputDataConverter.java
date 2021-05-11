package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.buscarlistagem.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.Reconhecimento;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.buscarlistagem.BuscarReconhecimentosOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

import java.util.stream.Collectors;

public class BuscarReconhecimentosOutputDataConverter {
    public BuscarReconhecimentosOutputData to(ListaPaginada<Reconhecimento> from) {
        BuscarReconhecimentosOutputDataItemConverter outputDataItemConverter = new BuscarReconhecimentosOutputDataItemConverter();
        return BuscarReconhecimentosOutputData
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

    private static class BuscarReconhecimentosOutputDataItemConverter extends GenericConverter<Reconhecimento, BuscarReconhecimentosOutputData.Item> {
    }
}
