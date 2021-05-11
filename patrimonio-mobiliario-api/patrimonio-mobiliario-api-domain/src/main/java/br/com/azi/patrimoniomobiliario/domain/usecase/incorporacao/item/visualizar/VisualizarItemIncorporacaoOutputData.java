package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.visualizar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisualizarItemIncorporacaoOutputData {
    private Long id;
    private String marca;
    private String modelo;
    private String fabricante;
    private Integer quantidade;
    private BigDecimal valorTotal;
    private String contaContabilDescricao;
    private String estadoConservacaoNome;
    private String tipoBem;
    private String descricao;
    private String combustivel;
    private String categoria;
    private String anoFabricacaoModelo;
    private String uriImagem;
    private String imagem;
}
