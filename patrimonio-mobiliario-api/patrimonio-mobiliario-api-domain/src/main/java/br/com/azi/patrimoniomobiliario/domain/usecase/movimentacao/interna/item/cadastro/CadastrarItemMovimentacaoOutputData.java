package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.cadastro;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CadastrarItemMovimentacaoOutputData {
    private List<Item> itens;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item {
        private Long movimentacaoId;
        private Long patrimonioId;
    }
}
