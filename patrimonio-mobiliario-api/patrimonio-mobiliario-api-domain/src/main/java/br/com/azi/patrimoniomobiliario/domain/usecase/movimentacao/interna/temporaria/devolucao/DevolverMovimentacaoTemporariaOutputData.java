package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.devolucao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DevolverMovimentacaoTemporariaOutputData {
    private Long id;
}
