package br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarsetoresnaoalmoxarifado;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuscarSetoresNaoAlmoxarifadoOutputData {

    List<BuscarSetoresNaoAlmoxarifadoOutputData.UnidadeOrganizacional> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UnidadeOrganizacional {
        private Long id;
        private String sigla;
        private String nome;
        private String codHierarquia;
        private String descricao;
    }
}
