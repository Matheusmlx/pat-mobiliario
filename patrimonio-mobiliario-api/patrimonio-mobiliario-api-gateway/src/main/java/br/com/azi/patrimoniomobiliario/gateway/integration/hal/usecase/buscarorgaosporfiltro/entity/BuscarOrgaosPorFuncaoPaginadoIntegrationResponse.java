package br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.buscarorgaosporfiltro.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuscarOrgaosPorFuncaoPaginadoIntegrationResponse {
    private List<EstruturaOrganizacional> content;
    private int totalElements;
    private int totalPages;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EstruturaOrganizacional {
        private Long id;
        private String sigla;
        private String nome;
        private String codigoHierarquia;
    }
}
