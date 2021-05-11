package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.buscarlistagem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuscarConveniosOutputData {

    private List<Item> items;
    private Long totalPages;
    private Long totalElements;

    @Data
    @NoArgsConstructor
    public static class Item {
        private Long id;
        private String numero;
        private String nome;
        private Concedente concedente;
        private String fonteRecurso;
        private String situacao;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Concedente {
        Long id;
        String nome;
    }
}
