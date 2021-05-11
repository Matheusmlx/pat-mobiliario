package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.existeporsituacao;

import br.com.azi.patrimoniomobiliario.domain.entity.ReservaIntervalo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExisteIntervalosPorSituacaoInputData {
    private Long reservaId;
    private ReservaIntervalo.Situacao situacao;
}
