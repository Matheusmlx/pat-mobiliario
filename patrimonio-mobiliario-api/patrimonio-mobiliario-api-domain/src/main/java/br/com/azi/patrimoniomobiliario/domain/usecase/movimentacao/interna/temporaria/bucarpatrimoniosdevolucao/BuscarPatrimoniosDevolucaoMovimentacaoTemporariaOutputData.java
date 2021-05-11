package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.bucarpatrimoniosdevolucao;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuscarPatrimoniosDevolucaoMovimentacaoTemporariaOutputData {
    private List<Item> itens;
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
    }
}
