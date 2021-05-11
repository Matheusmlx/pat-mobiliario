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
public class MemorandoMovimentacaoInterna {
    private String orgao;
    private String setorOrigem;
    private String setorDestino;
    private String mesAnoEnvio;
    private String numeroMemorando;
    private String numeroMovimentacao;
    private String dataEnvio;
    private String dataFinalizacao;
    private String dataEsperadaDevolucao;
    private String motivoObs;
    private String tipo;
    private BigDecimal valorEntrada;
    private BigDecimal valorDepreciadoAcumulado;
    private BigDecimal valorLiquido;
    private String numeroProcesso;

    private List<ContaContabil> contasContabeis;

    private Boolean emElaboracao;
    private Boolean possuiItens;
    private Boolean temporaria;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ContaContabil {
        private Long id;
        private String descricao;
        private BigDecimal valorEntrada;
        private BigDecimal valorDepreciadoAcumulado;
        private BigDecimal valorLiquido;
        private List<Patrimonio> patrimonios;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Patrimonio {
        private Long id;
        private String numero;
        private String descricao;
        private BigDecimal valorEntrada;
        private BigDecimal valorDepreciadoAcumulado;
        private BigDecimal valorLiquido;
        private String devolvidoNaData;
    }
}
