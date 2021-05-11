package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.devolucao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DevolverMovimentacaoTemporariaInputData {
    private Long movimentacaoId;
    private List<Long> patrimoniosId;
    private boolean devolverTodos;
}
