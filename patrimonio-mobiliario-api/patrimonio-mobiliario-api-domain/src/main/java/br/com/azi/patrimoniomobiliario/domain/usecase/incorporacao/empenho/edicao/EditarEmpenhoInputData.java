package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.edicao;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SuppressFBWarnings(value = "EI_EXPOSE_REP")
public class EditarEmpenhoInputData {
    private Long id;
    private String numeroEmpenho;
    private Date dataEmpenho;
    private BigDecimal valorEmpenho;
    private Long incorporacaoId;
}
