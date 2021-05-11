package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.editar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EditaItemIncorporacaoInputData {

    private Long id;
    private Long idIncorporacao;
    private String marca;
    private String modelo;
    private String fabricante;
    private Integer quantidade;
    private BigDecimal valorTotal;
    private String numeracaoPatrimonial;
    private Long naturezaDespesa;
    private Long estadoConservacao;
    private String situacao;
    private String uriImagem;
    private String anoFabricacaoModelo;
    private String combustivel;
    private String categoria;
    private String descricao;
    private String codigo;
}
