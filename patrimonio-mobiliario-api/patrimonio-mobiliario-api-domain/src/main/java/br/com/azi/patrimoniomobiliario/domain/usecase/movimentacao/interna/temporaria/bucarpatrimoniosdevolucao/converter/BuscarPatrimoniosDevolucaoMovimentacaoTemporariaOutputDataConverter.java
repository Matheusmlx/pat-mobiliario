package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.bucarpatrimoniosdevolucao.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.bucarpatrimoniosdevolucao.BuscarPatrimoniosDevolucaoMovimentacaoTemporariaOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

import java.util.stream.Collectors;

public class BuscarPatrimoniosDevolucaoMovimentacaoTemporariaOutputDataConverter {

    public BuscarPatrimoniosDevolucaoMovimentacaoTemporariaOutputData to(ListaPaginada<Patrimonio> from) {
        ItemConverter outputDataItemConverter = new ItemConverter();

        return BuscarPatrimoniosDevolucaoMovimentacaoTemporariaOutputData
            .builder()
            .totalElements(from.getTotalElements())
            .totalPages(from.getTotalPages())
            .itens(from
                .getItems()
                .stream()
                .map(outputDataItemConverter::to)
                .collect(Collectors.toList()))
            .build();
    }

    public static class ItemConverter extends GenericConverter<Patrimonio, BuscarPatrimoniosDevolucaoMovimentacaoTemporariaOutputData.Item> {
        @Override
        public BuscarPatrimoniosDevolucaoMovimentacaoTemporariaOutputData.Item to(Patrimonio source) {
            final BuscarPatrimoniosDevolucaoMovimentacaoTemporariaOutputData.Item target = super.to(source);

            target.setPatrimonioId(source.getId());
            target.setPatrimonioNumero(source.getNumero());
            target.setPatrimonioDescricao(source.getItemIncorporacao().getDescricao());

            return target;
        }
    }
}
