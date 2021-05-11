package br.com.azi.patrimoniomobiliario.domain.usecase.fornecedor.buscarlistagem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuscarFornecedoresOutputData {

    List<Item> items;
    Long totalPages;
    Long totalElements;

    @Data
    @NoArgsConstructor
    public static class Item {
        private Long id;
        private String nome;
        private String cpfCnpj;
    }
}
