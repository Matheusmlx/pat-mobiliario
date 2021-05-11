package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.cadastro.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.MovimentacaoItem;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.cadastro.CadastrarItemMovimentacaoOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

import java.util.List;
import java.util.stream.Collectors;

public class CadastrarItemMovimentacaoOutputDataConverter {

    public CadastrarItemMovimentacaoOutputData to(List<MovimentacaoItem> from) {
        CadastrarItemMovimentacaoOutputDataItemConverter itemConverter = new CadastrarItemMovimentacaoOutputDataItemConverter();

        return CadastrarItemMovimentacaoOutputData.builder()
            .itens(from
                .stream()
                .map(itemConverter::to)
                .collect(Collectors.toList()))
            .build();
    }

    private static class CadastrarItemMovimentacaoOutputDataItemConverter extends GenericConverter<MovimentacaoItem, CadastrarItemMovimentacaoOutputData.Item> {
        @Override
        public CadastrarItemMovimentacaoOutputData.Item to(MovimentacaoItem source) {
            CadastrarItemMovimentacaoOutputData.Item target = super.to(source);
            target.setMovimentacaoId(source.getMovimentacao().getId());
            target.setPatrimonioId(source.getPatrimonio().getId());

            return target;
        }
    }
}
