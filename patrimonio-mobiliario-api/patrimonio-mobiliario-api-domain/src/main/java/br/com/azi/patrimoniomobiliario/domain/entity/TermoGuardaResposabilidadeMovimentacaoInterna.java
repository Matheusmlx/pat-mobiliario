package br.com.azi.patrimoniomobiliario.domain.entity;

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
public class TermoGuardaResposabilidadeMovimentacaoInterna {
    private String orgao;
    private String numeroTermo;
    private String tipo;
    private String dataMovimentacao;
    private String setor;
    private String motivoObs;
    private List<Patrimonio> patrimonios;
    private int totalDeBens;
    private BigDecimal valorTotal;
    private String responsavel;


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Patrimonio {
        private Long id;
        private String numero;
        private String descricao;
        private BigDecimal valorLiquido;
    }
}
