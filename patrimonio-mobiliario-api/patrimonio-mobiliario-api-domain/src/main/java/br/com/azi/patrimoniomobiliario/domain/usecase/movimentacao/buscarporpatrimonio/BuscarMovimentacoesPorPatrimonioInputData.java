package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.buscarporpatrimonio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuscarMovimentacoesPorPatrimonioInputData {
    private Long patrimonioId;
}
