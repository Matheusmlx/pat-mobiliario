package br.com.azi.patrimoniomobiliario.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NaturezaDespesa {
    private Long id;
    private String despesa;
    private String descricao;
    private Situacao situacao;
    private ContaContabil contaContabil;

    public enum Situacao{
        ATIVO,
        INATIVO
    }

}
