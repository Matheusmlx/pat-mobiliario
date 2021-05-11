package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.buscarsituacao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuscarSituacaoMovimentacaoOutputData {
    private String situacao;
}
