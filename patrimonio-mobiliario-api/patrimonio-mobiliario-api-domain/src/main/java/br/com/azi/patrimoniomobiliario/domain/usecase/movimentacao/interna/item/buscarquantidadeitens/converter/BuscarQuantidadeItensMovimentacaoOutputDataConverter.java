package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.buscarquantidadeitens.converter;

import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.buscarquantidadeitens.BuscarQuantidadeItensMovimentacaoOutputData;

public class BuscarQuantidadeItensMovimentacaoOutputDataConverter {

    public BuscarQuantidadeItensMovimentacaoOutputData to(Long movimentacaoId, Long quantidadeItens) {
        return BuscarQuantidadeItensMovimentacaoOutputData.builder()
            .movimentacaoId(movimentacaoId)
            .quantidadeItens(quantidadeItens)
            .build();
    }

}
