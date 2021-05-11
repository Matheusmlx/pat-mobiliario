package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.buscarsituacao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuscarSituacaoMovimentacaoInputData {
    private Long movimentacaoId;
}
