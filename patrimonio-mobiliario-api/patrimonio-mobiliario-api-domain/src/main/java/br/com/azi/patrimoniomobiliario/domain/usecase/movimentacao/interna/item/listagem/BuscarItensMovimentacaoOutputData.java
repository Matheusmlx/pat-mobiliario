package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.listagem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuscarItensMovimentacaoOutputData {
    private List<BuscarItensMovimentacaoOutputData.Item> items;
    private Long totalPages;
    private Long totalElements;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item {
        private Long patrimonioId;
        private String patrimonioNumero;
        private String patrimonioDescricao;
        private String incorporacaoCodigo;
        private Long movimentacaoId;
        private String dataDevolucao;
    }
}
