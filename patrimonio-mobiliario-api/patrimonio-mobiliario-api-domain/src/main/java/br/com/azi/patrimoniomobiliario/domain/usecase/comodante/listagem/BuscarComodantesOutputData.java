package br.com.azi.patrimoniomobiliario.domain.usecase.comodante.listagem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuscarComodantesOutputData {

    private List<Item> items;
    private Long totalElements;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item {
        private Long id;
        private String nome;
    }

}
