package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.editar;

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
public class EditarDocumentoInputData {
    private Long id;
    private String numero;
    private Date data;
    private BigDecimal valor;
    private String uriAnexo;
    private Long incorporacao;
    private Long tipo;
}
