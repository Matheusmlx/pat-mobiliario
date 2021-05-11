package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemporincorporacao.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemporincorporacao.BuscarPatrimoniosIncorporacaoOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

import java.util.Objects;
import java.util.stream.Collectors;

public class BuscarPatrimoniosIncorporacaoOutputDataConverter {

    public BuscarPatrimoniosIncorporacaoOutputData to(ListaPaginada<Patrimonio> source, Long totalElementsOfAllPages) {
        BuscarPatrimonioIncorporacaoConverter itemConverter = new BuscarPatrimonioIncorporacaoConverter();

        return  BuscarPatrimoniosIncorporacaoOutputData
            .builder()
            .totalElements(source.getTotalElements())
            .totalPages(source.getTotalPages())
            .totalElementsOfAllPages(totalElementsOfAllPages)
            .items(source.getItems()
                .stream()
                .map(itemConverter::to)
                .collect(Collectors.toList()))
            .build();
    }

    public static class BuscarPatrimonioIncorporacaoConverter extends GenericConverter<Patrimonio, BuscarPatrimoniosIncorporacaoOutputData.Item> {

        @Override
        public BuscarPatrimoniosIncorporacaoOutputData.Item to(Patrimonio source) {
            BuscarPatrimoniosIncorporacaoOutputData.Item target = super.to(source);

            if (Objects.nonNull(source.getItemIncorporacao())) {
                target.setDescricao(source.getItemIncorporacao().getDescricao());
            }

            if (Objects.nonNull(source.getValorAquisicao())) {
                target.setValor(source.getValorAquisicao());
            }

            return target;
        }
    }

}
