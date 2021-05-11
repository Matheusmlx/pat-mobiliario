package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.cadastrar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CadastrarReservaIntervaloInputData {

    private Long reservaId;
    private List<Intervalo> intervalos;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Intervalo {
        private Long orgaoId;
        private String preenchimento;
        private Long quantidadeReservada;
        private Long numeroInicio;
        private Long numeroFim;
    }

}
