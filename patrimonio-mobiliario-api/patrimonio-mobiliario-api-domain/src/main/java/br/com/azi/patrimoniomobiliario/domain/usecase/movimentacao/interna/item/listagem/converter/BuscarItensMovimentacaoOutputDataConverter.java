package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.listagem.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.MovimentacaoItem;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.listagem.BuscarItensMovimentacaoOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import br.com.azi.patrimoniomobiliario.utils.validate.DateValidate;

import java.util.Objects;
import java.util.stream.Collectors;

public class BuscarItensMovimentacaoOutputDataConverter {

    public BuscarItensMovimentacaoOutputData to(ListaPaginada<MovimentacaoItem> from) {
        BuscarItensMovimentacaoOutputDataItemConverter outputDataItemConverter = new BuscarItensMovimentacaoOutputDataItemConverter();

        return BuscarItensMovimentacaoOutputData
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

    public static class BuscarItensMovimentacaoOutputDataItemConverter extends GenericConverter<MovimentacaoItem, BuscarItensMovimentacaoOutputData.Item> {
        @Override
        public BuscarItensMovimentacaoOutputData.Item to(MovimentacaoItem source) {
            final BuscarItensMovimentacaoOutputData.Item target = super.to(source);

            target.setPatrimonioId(source.getPatrimonio().getId());
            target.setPatrimonioNumero(source.getPatrimonio().getNumero());
            target.setPatrimonioDescricao(source.getPatrimonio().getItemIncorporacao().getDescricao());
            target.setIncorporacaoCodigo(source.getPatrimonio().getItemIncorporacao().getIncorporacao().getCodigo());
            target.setMovimentacaoId(source.getMovimentacao().getId());

            if (Objects.nonNull(source.getDataDevolucao())) {
                target.setDataDevolucao(DateValidate.formatarData(source.getDataDevolucao()));
            }

            return target;
        }
    }
}
