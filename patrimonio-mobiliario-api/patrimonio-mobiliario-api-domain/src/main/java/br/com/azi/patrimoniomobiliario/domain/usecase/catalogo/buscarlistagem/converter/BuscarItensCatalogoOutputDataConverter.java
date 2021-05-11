package br.com.azi.patrimoniomobiliario.domain.usecase.catalogo.buscarlistagem.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.ItemCatalogo;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.usecase.catalogo.buscarlistagem.BuscarItensCatalogoOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

import java.util.Objects;
import java.util.stream.Collectors;

public class BuscarItensCatalogoOutputDataConverter{

    public BuscarItensCatalogoOutputData to(ListaPaginada<ItemCatalogo> from){
        BuscarItensCatalogoOutputDataConverter.BuscarItensCatalogoOutputDataItemConverter outputDataItemConverter = new BuscarItensCatalogoOutputDataConverter.BuscarItensCatalogoOutputDataItemConverter();

        return BuscarItensCatalogoOutputData
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

    private static class BuscarItensCatalogoOutputDataItemConverter extends GenericConverter< ItemCatalogo, BuscarItensCatalogoOutputData.Item> {
        @Override
        public BuscarItensCatalogoOutputData.Item to(ItemCatalogo source) {
            BuscarItensCatalogoOutputData.Item target = super.to(source);

            target.setDescricao(source.getDescricao());

            if(Objects.nonNull(source.getId())){
                target.setId(source.getId());
            }

            if(Objects.nonNull(source.getCodigo())){
                target.setId(source.getId());
            }
            return target;
        }
    }
}
