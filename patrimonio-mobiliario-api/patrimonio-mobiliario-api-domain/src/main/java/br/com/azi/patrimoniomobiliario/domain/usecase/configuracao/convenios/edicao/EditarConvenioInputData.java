package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.edicao;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SuppressFBWarnings(value = "EI_EXPOSE_REP")
public class EditarConvenioInputData {
    private Long id;
    private String numero;
    private String nome;
    private Long concedente;
    private String fonteRecurso;
    private Date dataVigenciaInicio;
    private Date dataVigenciaFim;
    private String situacao;
}
