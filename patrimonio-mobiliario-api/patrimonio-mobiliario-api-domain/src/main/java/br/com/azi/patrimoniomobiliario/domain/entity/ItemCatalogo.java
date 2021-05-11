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
public class ItemCatalogo implements ItemCatalogoInterface {
    private Long id;
    private String situacao;
    private String codigo;
    private String tipo;
    private String descricao;
    private String status;
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
    }
}
