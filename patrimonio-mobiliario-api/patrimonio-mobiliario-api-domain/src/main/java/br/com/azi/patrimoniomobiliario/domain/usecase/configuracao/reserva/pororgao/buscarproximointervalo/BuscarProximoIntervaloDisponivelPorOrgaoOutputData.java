package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.buscarproximointervalo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuscarProximoIntervaloDisponivelPorOrgaoOutputData {
    private Long numeroInicio;
    private Long numeroFim;
}
