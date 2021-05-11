package br.com.azi.patrimoniomobiliario.gateway.integration.efornecedor.usecase.buscarporfiltro.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuscarFornecedoresPorFiltroIntegrationResponse {

    private List<Item> content;
    private Long totalPages;
    private Long totalElements;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class Item {
        private Long id;
        private String razaoSocial;
        private String cpfCnpj;
        private String situacao;
    }
}
