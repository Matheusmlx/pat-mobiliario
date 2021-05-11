package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.edicao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EditarConvenioOutputData {
    private Long id;
    private String numero;
    private String nome;
    private Long concedente;
    private String fonteRecurso;
    private LocalDateTime dataVigenciaInicio;
    private LocalDateTime dataVigenciaFim;
    private String situacao;
}
