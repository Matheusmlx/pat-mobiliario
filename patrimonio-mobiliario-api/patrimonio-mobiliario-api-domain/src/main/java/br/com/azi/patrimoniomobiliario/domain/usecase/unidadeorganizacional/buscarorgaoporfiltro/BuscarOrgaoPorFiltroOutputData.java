package br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarorgaoporfiltro;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuscarOrgaoPorFiltroOutputData {
    private List<UnidadeOrganizacional> items;
    private Long totalElements;
    private Long totalPages;

    @Data
    @NoArgsConstructor
    public static class UnidadeOrganizacional {
        private Long id;
        private String sigla;
        private String nome;
        private String codHierarquia;
        private String descricao;
    }
}
