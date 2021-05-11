package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.buscarquantidadeitens;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuscarQuantidadeItensMovimentacaoOutputData {
    private Long movimentacaoId;
    private Long quantidadeItens;
}
