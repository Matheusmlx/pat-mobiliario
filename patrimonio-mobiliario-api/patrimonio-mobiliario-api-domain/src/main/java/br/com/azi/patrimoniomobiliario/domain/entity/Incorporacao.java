package br.com.azi.patrimoniomobiliario.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Incorporacao {
    private Long id;
    private String codigo;
    private LocalDateTime dataRecebimento;
    private Situacao situacao;
    private String numProcesso;
    private String justificativa;
    private Fornecedor fornecedor;
    private String nota;
    private BigDecimal valorNota;
    private LocalDateTime dataNota;
    private UnidadeOrganizacional orgao;
    private UnidadeOrganizacional setor;
    private UnidadeOrganizacional fundo;
    private Convenio convenio;
    private String projeto;
    private Usuario usuario;
    private Reconhecimento reconhecimento;
    private Empenho empenho;
    private NaturezaDespesa naturezaDespesa;
    private Boolean origemConvenio;
    private Boolean origemFundo;
    private Boolean origemProjeto;
    private Boolean origemComodato;
    private String usuarioFinalizacao;
    private LocalDateTime dataFinalizacao;
    private NotaLancamentoContabil notaLancamentoContabil;
    private Comodante comodante;
    private LocalDateTime dataInicioProcessamento;
    private LocalDateTime dataFimProcessamento;
    private String erroProcessamento;
    private LocalDateTime dataCadastro;
    private LocalDateTime dataAlteracao;
    private String usuarioCadastro;
    private String usuarioAlteracao;

    public enum Situacao {
        EM_ELABORACAO,
        ESTORNADO,
        PARCIALMENTE_ESTORNADO,
        FINALIZADO,
        EM_PROCESSAMENTO,
        ERRO_PROCESSAMENTO
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Filtro extends  FiltroBase{
        private String conteudo;
        private String situacao;
        private Long usuarioId;
        private List<String> funcoes;
    }

}
