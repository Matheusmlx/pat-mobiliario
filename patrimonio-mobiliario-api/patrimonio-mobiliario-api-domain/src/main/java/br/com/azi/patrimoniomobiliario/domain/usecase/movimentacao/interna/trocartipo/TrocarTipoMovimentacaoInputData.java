package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.trocartipo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrocarTipoMovimentacaoInputData {
    private Long id;
    private String tipo;
}
