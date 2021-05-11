package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.buscartipo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuscarTipoMovimentacaoOutputData {
    private String tipo;
}
