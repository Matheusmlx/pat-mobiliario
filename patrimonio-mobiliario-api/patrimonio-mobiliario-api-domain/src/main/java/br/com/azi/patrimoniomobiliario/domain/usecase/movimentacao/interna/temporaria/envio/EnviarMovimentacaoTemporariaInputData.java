package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.envio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnviarMovimentacaoTemporariaInputData {
    private Long id;
}
