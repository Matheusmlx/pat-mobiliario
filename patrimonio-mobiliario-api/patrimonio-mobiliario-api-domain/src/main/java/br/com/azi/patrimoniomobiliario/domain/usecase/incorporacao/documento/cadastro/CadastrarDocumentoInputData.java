package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.cadastro;

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
public class CadastrarDocumentoInputData {
    private String numero;
    private BigDecimal valor;
    private Date data;
    private String uriAnexo;
    private Long tipo;
    private Long incorporacao;
}
