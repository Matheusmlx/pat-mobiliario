package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.finalizar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinalizarReservaPatrimonialInputData {
    private Long reservaId;
}