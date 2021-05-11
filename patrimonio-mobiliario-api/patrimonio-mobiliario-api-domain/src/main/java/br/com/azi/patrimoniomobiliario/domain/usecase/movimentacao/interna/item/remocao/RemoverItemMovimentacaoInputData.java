package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.remocao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RemoverItemMovimentacaoInputData {
    private Long patrimonioId;
    private Long movimentacaoId;
}
