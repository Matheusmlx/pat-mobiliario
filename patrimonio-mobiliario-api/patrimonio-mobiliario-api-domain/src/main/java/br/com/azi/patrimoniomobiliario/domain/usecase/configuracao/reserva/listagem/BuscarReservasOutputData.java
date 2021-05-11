package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.listagem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuscarReservasOutputData {
    private List<Item> items;
    private Long totalPages;
    private Long totalElements;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item {
        private Long id;
        private String codigo;
        private String dataCriacao;
        private Long quantidadeReservada;
        private Long quantidadeRestante;
        private Set<String> orgaos;
        private String situacao;
        private Long numeroInicio;
        private Long numeroFim;
    }
}
