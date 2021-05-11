package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.listagem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuscarReservaIntervalosOutputData {
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
        private String orgao;
        private String situacao;
        private Long quantidadeReservada;
        private String preenchimento;
        private Long numeroInicio;
        private Long numeroFim;
    }
}
