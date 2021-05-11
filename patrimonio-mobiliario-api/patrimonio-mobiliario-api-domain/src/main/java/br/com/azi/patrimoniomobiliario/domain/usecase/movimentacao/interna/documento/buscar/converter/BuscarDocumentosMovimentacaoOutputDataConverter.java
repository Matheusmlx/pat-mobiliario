package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.buscar.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Documento;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.buscar.BuscarDocumentosMovimentacaoOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import br.com.azi.patrimoniomobiliario.utils.validate.DateValidate;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BuscarDocumentosMovimentacaoOutputDataConverter {

    public BuscarDocumentosMovimentacaoOutputData to(List<Documento> from) {
        final BuscarDocumentosMovimentacaoOutputDataItemConverter itemConverter = new BuscarDocumentosMovimentacaoOutputDataItemConverter();

        List<BuscarDocumentosMovimentacaoOutputData.Documento> items = from.stream()
            .map(itemConverter::to)
            .collect(Collectors.toList());

        return new BuscarDocumentosMovimentacaoOutputData(items);
    }

    public static class BuscarDocumentosMovimentacaoOutputDataItemConverter extends GenericConverter<Documento, BuscarDocumentosMovimentacaoOutputData.Documento> {
        @Override
        public BuscarDocumentosMovimentacaoOutputData.Documento to(Documento source) {
            BuscarDocumentosMovimentacaoOutputData.Documento target = super.to(source);

            if (Objects.nonNull(source.getMovimentacao().getId())) {
                target.setMovimentacao(source.getMovimentacao().getId());
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
