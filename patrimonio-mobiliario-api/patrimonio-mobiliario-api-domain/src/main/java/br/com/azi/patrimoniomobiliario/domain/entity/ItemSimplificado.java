package br.com.azi.patrimoniomobiliario.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemSimplificado implements ItemCatalogoInterface {
    private Long id;
    private String codigo;
    private String descricao;
    private String situacao;
    private String status;
    private String tipo;
    private Long materialServicoId;

    public enum Situacao{
        ATIVO,
        INATIVO
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Filtro extends FiltroBase{
        String conteudo;
        Long produtoId;
    }
}
