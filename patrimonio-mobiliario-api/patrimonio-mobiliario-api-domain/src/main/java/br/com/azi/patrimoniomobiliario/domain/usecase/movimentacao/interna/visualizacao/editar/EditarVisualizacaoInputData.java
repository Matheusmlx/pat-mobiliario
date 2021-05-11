package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.visualizacao.editar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditarVisualizacaoInputData {
    private Long id;
    private String numeroNotaLancamentoContabil;
    private Date dataNotaLancamentoContabil;
}
