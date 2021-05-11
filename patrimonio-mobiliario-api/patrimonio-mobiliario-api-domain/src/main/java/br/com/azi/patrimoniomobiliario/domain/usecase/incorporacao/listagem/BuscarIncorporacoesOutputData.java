package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.listagem;

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
public class BuscarIncorporacoesOutputData {

    List<Item> items;
    Long totalPages;
    Long totalElements;

    @Data
    @NoArgsConstructor
    public static class Item{
        private Long id;
        private String codigo;
        private String dataRecebimento;
        private String situacao;
        private String numProcesso;
        private String nota;
        private BigDecimal valorNota;
        private String dataNota;
        private String fornecedor;
        private String orgao;
        private Long setor;
        private Long convenio;
        private Reconhecimento reconhecimento;
        private Long fundo;
        private Long naturezaDespesa;
        private Boolean origemConvenio;
        private Boolean origemFundo;
        private Boolean origemProjeto;
        private String projeto;
        private String numeroNotaLancamentoContabil;
        private String dataNotaLancamentoContabil;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Reconhecimento{
            private Long id;
            private Boolean notaFiscal;
            private Boolean empenho;
        }
    }
}
