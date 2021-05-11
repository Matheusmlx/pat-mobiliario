package br.com.azi.patrimoniomobiliario.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Concedente {
    private Long id;
    private String cpfCnpj;
    private Pessoa tipoPessoa;
    private String nome;
    private Boolean inclusaoSistema;
    private Situacao situacao;
    private LocalDateTime dataCadastro;
    private LocalDateTime dataAlteracao;
    private String usuarioCadastro;
    private String usuarioAlteracao;

    public enum Situacao {
        ATIVO,
        INATIVO
    }

    public enum Pessoa {
        FISICA,
        JURIDICA
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Filtro extends FiltroBase {
        String conteudo;
        String situacao;
    }
}
