package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.buscar.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Documento;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.buscar.BuscarDocumentosOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import br.com.azi.patrimoniomobiliario.utils.validate.DateValidate;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BuscarDocumentosOutputDataConverter {

    private static final BuscarDocumentosOutputDataConverter.ItemOutputDataConverter ITEM_OUTPUT_DATA_CONVERTER = new BuscarDocumentosOutputDataConverter.ItemOutputDataConverter();

    public BuscarDocumentosOutputData to(List<Documento> source) {
        List<BuscarDocumentosOutputData.Documento> itens = source.stream()
            .map(this::converterItem)
            .collect(Collectors.toList());

        BuscarDocumentosOutputData target = new BuscarDocumentosOutputData();
        target.setItems(itens);

        return target;
    }

    private BuscarDocumentosOutputData.Documento converterItem(Documento source) {
        return ITEM_OUTPUT_DATA_CONVERTER.to(source);
    }

    private static class ItemOutputDataConverter extends GenericConverter<Documento, BuscarDocumentosOutputData.Documento> {
        @Override
        public BuscarDocumentosOutputData.Documento to(Documento source) {
            BuscarDocumentosOutputData.Documento target = super.to(source);

            if (Objects.nonNull(source.getIncorporacao().getId())) {
                target.setIncorporacao(source.getIncorporacao().getId());
            }
            if (Objects.nonNull(source.getTipo().getId())) {
                target.setTipo(source.getTipo().getId());
            }
            if (Objects.nonNull(source.getData())) {
                target.setData(DateValidate.formatarData(source.getData()));
            }

            return target;

        }
    }

}
