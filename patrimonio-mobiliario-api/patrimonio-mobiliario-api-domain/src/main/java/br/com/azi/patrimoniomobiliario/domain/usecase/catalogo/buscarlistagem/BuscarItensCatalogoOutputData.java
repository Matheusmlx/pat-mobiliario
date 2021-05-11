package br.com.azi.patrimoniomobiliario.domain.usecase.catalogo.buscarlistagem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuscarItensCatalogoOutputData {
    Long totalPages;
    Long totalElements;
    List<Item> items;

    @Data
    @NoArgsConstructor
    public static class Item{
        private Long id;
        private String codigo;
        private String descricao;
    }
}
