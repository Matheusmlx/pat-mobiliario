package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.listagem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuscarMovimentacoesInternasOutputData {

    private List<Item> items;
    private Long totalElements;
    private Long totalPages;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item {
        private Long id;
        private String codigo;
        private String tipo;
        private String orgao;
        private String setorOrigem;
        private String setorDestino;
        private String motivoObservacao;
        private String numeroNotaLancamentoContabil;
        private String dataNotaLancamentoContabil;
        private String dataDevolucao;
        private String numeroProcesso;
        private String situacao;
        private Long responsavel;
    }

}
