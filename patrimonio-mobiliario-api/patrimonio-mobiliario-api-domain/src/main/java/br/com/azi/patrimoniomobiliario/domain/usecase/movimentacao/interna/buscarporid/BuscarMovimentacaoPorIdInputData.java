package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.buscarporid;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuscarMovimentacaoPorIdInputData {
    private Long id;
}
