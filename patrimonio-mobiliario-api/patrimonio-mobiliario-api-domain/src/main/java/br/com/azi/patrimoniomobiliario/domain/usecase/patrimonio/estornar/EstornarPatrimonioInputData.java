package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.estornar;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SuppressFBWarnings(value = "EI_EXPOSE_REP")
public class EstornarPatrimonioInputData {
    private List<Long> patrimoniosId;
    private List<Long> patrimoniosExcecao;
    private Long incorporacaoId;
    private String motivo;
    private Date data;
    private String usuario;
    private Boolean todosSelecionados;
}
