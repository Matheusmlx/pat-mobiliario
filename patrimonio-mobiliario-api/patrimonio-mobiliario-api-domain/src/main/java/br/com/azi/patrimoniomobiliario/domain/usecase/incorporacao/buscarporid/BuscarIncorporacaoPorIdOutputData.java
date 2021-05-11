package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.buscarporid;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuscarIncorporacaoPorIdOutputData {
    private Long id;
    private String codigo;
    private String dataRecebimento;
    private String situacao;
    private String numProcesso;
    private String nota;
    private BigDecimal valorNota;
    private String dataNota;
    private Fornecedor fornecedor;
    private Orgao orgao;
    private Setor setor;
    private Long fundo;
    private Long convenio;
    private String projeto;
    private Reconhecimento reconhecimento;
    private Long empenho;
    private Long naturezaDespesa;
    private Boolean origemConvenio;
    private Boolean origemFundo;
    private Boolean origemProjeto;
    private Boolean origemComodato;
    private String numeroNotaLancamentoContabil;
    private String dataNotaLancamentoContabil;
    private Comodante comodante;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Reconhecimento {
        private Long id;
        private String nome;
        private Situacao situacao;
        private Boolean execucaoOrcamentaria;
        private Boolean notaFiscal;
        private Boolean empenho;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Fornecedor {
        private Long id;
        private String nome;
        private String cpfCnpj;
        private Situacao situacao;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Orgao {
        private Long id;
        private String descricao;
        private Situacao situacao;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Setor {
        private Long id;
        private String descricao;
        private Situacao situacao;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Comodante {
        private Long id;
        private String nome;
    }

    public enum Situacao {
        ATIVO,
        INATIVO
    }
}
