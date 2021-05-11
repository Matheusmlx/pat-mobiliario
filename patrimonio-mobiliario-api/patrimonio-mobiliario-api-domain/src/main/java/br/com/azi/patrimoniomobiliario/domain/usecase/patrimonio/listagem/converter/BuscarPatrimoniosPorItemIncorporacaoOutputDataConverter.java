package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagem.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagem.BuscarPatrimoniosPorItemIncorporacaoOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

import java.util.Objects;
import java.util.stream.Collectors;

public class BuscarPatrimoniosPorItemIncorporacaoOutputDataConverter {

    public BuscarPatrimoniosPorItemIncorporacaoOutputData to(ListaPaginada<Patrimonio> from) {
        BuscarPatrimonioOutputDataConverter outputDataConverter = new BuscarPatrimonioOutputDataConverter();

        return BuscarPatrimoniosPorItemIncorporacaoOutputData
            .builder()
            .totalElements(from.getTotalElements())
            .totalPages(from.getTotalPages())
            .items(from
                .getItems()
                .stream()
                .map(outputDataConverter::to)
                .collect(Collectors.toList()))
            .build();
    }

    public static class BuscarPatrimonioOutputDataConverter extends GenericConverter<Patrimonio, BuscarPatrimoniosPorItemIncorporacaoOutputData.Item> {

        @Override
        public BuscarPatrimoniosPorItemIncorporacaoOutputData.Item to(Patrimonio source) {
            BuscarPatrimoniosPorItemIncorporacaoOutputData.Item target = super.to(source);

            if(Objects.nonNull(source.getContaContabilClassificacao())) {
                target.setContaContabil(source.getContaContabilClassificacao().getId());
            }

            return target;
        }
    }
}
