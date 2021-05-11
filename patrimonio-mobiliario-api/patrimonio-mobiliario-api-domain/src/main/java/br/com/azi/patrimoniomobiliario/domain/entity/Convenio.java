package br.com.azi.patrimoniomobiliario.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Convenio {

    private Long id;
    private String numero;
    private String nome;
    private String fonteRecurso;
    private LocalDateTime dataVigenciaInicio;
    private LocalDateTime dataVigenciaFim;
    private Situacao situacao;
    private String tipo;
    private Concedente concedente;
    private LocalDateTime dataCadastro;
    private LocalDateTime dataAlteracao;
    private String usuarioCadastro;
    private String usuarioAlteracao;

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
        String situacao;
        List<Long> unidadeOrganizacionalIds;
    }
}
