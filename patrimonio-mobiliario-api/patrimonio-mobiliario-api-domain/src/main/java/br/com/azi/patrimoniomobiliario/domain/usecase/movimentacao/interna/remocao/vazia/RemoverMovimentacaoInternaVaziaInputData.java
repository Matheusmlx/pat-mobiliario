package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.remocao.vazia;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RemoverMovimentacaoInternaVaziaInputData {
    private Long movimentacaoId;
}
