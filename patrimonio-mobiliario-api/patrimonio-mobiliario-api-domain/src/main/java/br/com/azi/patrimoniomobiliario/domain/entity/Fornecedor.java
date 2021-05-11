package br.com.azi.patrimoniomobiliario.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Fornecedor {

    private Long id;
    private String cpfCnpj;
    private String nome;
    private Situacao situacao;

    public enum Situacao {
        ATIVO,
        INATIVO
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Filtro extends FiltroBase {
        String conteudo;
    }
}
