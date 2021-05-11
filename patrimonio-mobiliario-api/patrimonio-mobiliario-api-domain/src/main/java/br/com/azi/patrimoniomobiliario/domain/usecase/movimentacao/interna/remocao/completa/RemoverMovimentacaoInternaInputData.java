package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.remocao.completa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RemoverMovimentacaoInternaInputData {
    private Long movimentacaoId;
}
