package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.listagem.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.ItemIncorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.listagem.BuscarItensIncorporacaoOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

import java.util.Objects;
import java.util.stream.Collectors;

public class BuscarItensIncorporacaoOutputDataConverter {

    public BuscarItensIncorporacaoOutputData to(ListaPaginada<ItemIncorporacao> from) {
        BuscarItensIncorporacaoOutputDataItemConverter outputDataItemConverter = new BuscarItensIncorporacaoOutputDataItemConverter();

        return BuscarItensIncorporacaoOutputData
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

    private static class BuscarItensIncorporacaoOutputDataItemConverter extends GenericConverter<ItemIncorporacao, BuscarItensIncorporacaoOutputData.Item> {

        @Override
        public BuscarItensIncorporacaoOutputData.Item to(ItemIncorporacao source) {
            BuscarItensIncorporacaoOutputData.Item target = super.to(source);

            if(Objects.nonNull(source.getContaContabil())){
                target.setContaContabil(source.getContaContabil().getId());
            }

            if(Objects.nonNull(source.getNaturezaDespesa())){
                target.setNaturezaDespesa(source.getNaturezaDespesa().getId());
            }

            if(Objects.nonNull(source.getEstadoConservacao())) {
                target.setEstadoConservacao(source.getEstadoConservacao().getId());
            }
            return target;
        }
    }
}
