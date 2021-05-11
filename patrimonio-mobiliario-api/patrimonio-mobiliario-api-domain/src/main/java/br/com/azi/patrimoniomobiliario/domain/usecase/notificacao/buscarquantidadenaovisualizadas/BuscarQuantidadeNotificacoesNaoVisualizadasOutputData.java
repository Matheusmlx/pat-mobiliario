package br.com.azi.patrimoniomobiliario.domain.usecase.notificacao.buscarquantidadenaovisualizadas;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuscarQuantidadeNotificacoesNaoVisualizadasOutputData {
    private Long quantidadeNaoVisualizadas;
}
