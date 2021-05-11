package br.com.azi.patrimoniomobiliario.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UnidadeOrganizacional {
    private Long id;
    private String nome;
    private String sigla;
    private String codHierarquia;
    private String descricao;
    private Situacao situacao;
    private Boolean almoxarifado;

    public enum Situacao {
        ATIVO,
        INATIVO
    }
}
