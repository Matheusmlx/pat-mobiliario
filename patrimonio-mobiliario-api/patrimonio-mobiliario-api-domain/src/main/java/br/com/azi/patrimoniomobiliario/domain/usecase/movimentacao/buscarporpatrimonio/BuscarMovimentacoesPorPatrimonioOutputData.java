package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.buscarporpatrimonio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuscarMovimentacoesPorPatrimonioOutputData {
    private List<Item> items;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item {
        private Long id;
        private String tipo;
        private String dataFinalizacao;
        private String dataCriacao;
        private BigDecimal valorTotal;
        private String situacao;
        private String orgao;
        private String setorOrigem;
        private String setorDestino;
        private String motivoObservacao;
        private String numeroNotaLancamentoContabil;
        private String dataNotaLancamentoContabil;
        private String numeroProcesso;
    }
}
