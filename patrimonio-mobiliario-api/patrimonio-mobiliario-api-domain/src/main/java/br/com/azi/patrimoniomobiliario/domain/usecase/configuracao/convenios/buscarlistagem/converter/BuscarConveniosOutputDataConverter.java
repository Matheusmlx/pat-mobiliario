package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.buscarlistagem.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Convenio;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.buscarlistagem.BuscarConveniosOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

import java.util.Objects;
import java.util.stream.Collectors;

public class BuscarConveniosOutputDataConverter {

    public BuscarConveniosOutputData to(ListaPaginada<Convenio> from) {
        BuscarConveniosOutputDataItemConverter outputDataItemConverter = new BuscarConveniosOutputDataItemConverter();

        return BuscarConveniosOutputData
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

    private static class BuscarConveniosOutputDataItemConverter extends GenericConverter<Convenio, BuscarConveniosOutputData.Item> {

        @Override
        public BuscarConveniosOutputData.Item to(Convenio source) {
            BuscarConveniosOutputData.Item target = super.to(source);

            if (Objects.nonNull(source.getSituacao())) {
                target.setSituacao(source.getSituacao().toString());
            }

            if (Objects.nonNull(source.getConcedente())) {
                target.setConcedente(BuscarConveniosOutputData.Concedente.builder()
                    .id(source.getConcedente().getId())
                    .nome(source.getConcedente().getNome()).build());
            }
            return target;
        }
    }
}

