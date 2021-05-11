package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.edicao;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SuppressFBWarnings(value = "EI_EXPOSE_REP")
public class EditarIncorporacaoInputData {
    private Long id;
    private String codigo;
    private Date dataRecebimento;
    private String numProcesso;
    private String nota;
    private BigDecimal valorNota;
    private Date dataNota;
    private String numeroNotaLancamentoContabil;
    private Date dataNotaLancamentoContabil;
    private Long fornecedor;
    private Long orgao;
    private Long setor;
    private Long fundo;
    private Long convenio;
    private String projeto;
    private Long usuario;
    private Long reconhecimento;
    private Long empenho;
    private Long naturezaDespesa;
    private Long comodante;
    private Boolean origemConvenio;
    private Boolean origemFundo;
    private Boolean origemProjeto;
    private Boolean origemComodato;
}
