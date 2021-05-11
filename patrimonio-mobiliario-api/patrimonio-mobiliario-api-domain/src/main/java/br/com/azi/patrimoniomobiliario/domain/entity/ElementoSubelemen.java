package br.com.azi.patrimoniomobiliario.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElementoSubelemen {
    private Long id;
    private String codigo;
    private String nome;
    private String situacao;
    private String tipo;
    private String justificativa;
    private ElementoSubelemen pai;
}
