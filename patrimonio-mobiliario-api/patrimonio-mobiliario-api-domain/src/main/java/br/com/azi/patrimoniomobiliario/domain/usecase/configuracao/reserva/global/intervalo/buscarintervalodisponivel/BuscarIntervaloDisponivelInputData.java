package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.buscarintervalodisponivel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuscarIntervaloDisponivelInputData {
    private List<Item> items;
    private Long reservaId;
    private Long orgaoId;
    private Long quantidadeReservada;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item {
        private Long orgaoId;
        private Long quantidadeReservada;
        private Long numeroFim;
    }
}
