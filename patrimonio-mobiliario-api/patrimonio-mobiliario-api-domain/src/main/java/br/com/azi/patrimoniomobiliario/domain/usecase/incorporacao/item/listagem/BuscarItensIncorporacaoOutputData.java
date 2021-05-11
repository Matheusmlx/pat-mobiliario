package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.listagem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuscarItensIncorporacaoOutputData {
    private List<Item> items;
    private Long totalPages;
    private Long totalElements;

    @Data
    @NoArgsConstructor
    public static class Item {
        private Long id;
        private String marca;
        private String modelo;
        private String fabricante;
        private Integer quantidade;
        private BigDecimal valorTotal;
        private String numeracaoPatrimonial;
        private String tipoBem;
        private String uriImagem;
        private String anoFabricacaoModelo;
        private String combustivel;
        private String categoria;
        private String descricao;
        private String codigo;
        private String imagem;
        private String situacao;
        private Long contaContabil;
        private Long naturezaDespesa;
        private Long estadoConservacao;
    }
}
