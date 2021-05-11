package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.buscarporid;

import br.com.azi.patrimoniomobiliario.domain.entity.ConfiguracaoDepreciacao;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuscarPatrimonioPorIdOutputData {
    private Long id;
    private String numero;
    private LocalDateTime dataIncorporado;
    private String situacao;
    private String imagem;
    private String uriImagem;
    private String descricao;
    private String orgao;
    private String setor;
    private String reconhecimento;
    private String convenio;
    private String fundo;
    private String projeto;
    private String comodante;
    private String numeroSerie;
    private BigDecimal valorAquisicao;
    private BigDecimal valorResidual;
    private String naturezaDespesa;
    private String contaContabilClassificacao;
    private String contaContabilAtual;
    private String tipo;
    private String estadoConservacao;
    private String marca;
    private String modelo;
    private String fabricante;
    private String anoFabricacaoModelo;
    private String combustivel;
    private String categoria;
    private String placa;
    private String renavam;
    private String licenciamento;
    private String motor;
    private String chassi;
    private Integer prazoVidaUtil;
    private BigDecimal valorLiquido;
    private String metodo;
    private BigDecimal valorDepreciacaoMensal;
    private BigDecimal valorDepreciacaoAcumulado;
    private Incorporacao incorporacao;
    private DadosDepreciacao dadosDepreciacao;
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Incorporacao {
        private Long incorporacaoId;
        private Tipo tipo;
        private BigDecimal valorTotal;
        private String situacao;
        private String numeroNotaLancamentoContabil;
        private String dataFinalizacao;
        private String dataCriacao;

        public enum Tipo {
            INCORPORACAO
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DadosDepreciacao {
        private Long id;
        private String metodo;
        private Integer vidaUtil;
        private BigDecimal valorDepreciacaoMensal;
        private BigDecimal valorDepreciacaoAcumulado;
    }

}
