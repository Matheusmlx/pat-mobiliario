package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.buscarporid;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuscarItemIncorporacaoPorIdOutputData {
    private Long id;
    private String marca;
    private String modelo;
    private String fabricante;
    private Integer quantidade;
    private BigDecimal valorTotal;
    private String numeracaoPatrimonial;
    private String tipoBem;
    private String uriImagem;
    private String situacao;
    private String anoFabricacaoModelo;
    private String combustivel;
    private String categoria;
    private String descricao;
    private String codigo;
    private ContaContabil contaContabil;
    private NaturezaDespesa naturezaDespesa;
    private Long estadoConservacao;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ContaContabil {
        Long id;
        String codigo;
        String descricao;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NaturezaDespesa{
        Long id;
        String despesa;
        String descricao;
        Situacao situacao;

        public enum Situacao{
            ATIVO,
            INATIVO
        }
    }

}
