package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.intervalo.relatorios.termoresponsabilidade;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GerarTermoGuardaResponsabilidadeReservaInputData {
    private Long reservaId;
    private Long reservaIntervaloId;
}
