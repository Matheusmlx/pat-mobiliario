package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.buscarlistagem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuscarReconhecimentosOutputData {
    private List<Item> items;
    private Long totalPages;
    private Long totalElements;

    @Data
    @NoArgsConstructor
    public static class Item {
        private Long id;
        private String nome;
        private String situacao;
        private Boolean execucaoOrcamentaria;
        private Boolean notaFiscal;
        private Boolean empenho;
    }
}
