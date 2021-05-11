package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.buscarporid;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuscarItemIncorporacaoPorIdInputData {
    private Long idItem;
    private Long idIncorporacao;
}
