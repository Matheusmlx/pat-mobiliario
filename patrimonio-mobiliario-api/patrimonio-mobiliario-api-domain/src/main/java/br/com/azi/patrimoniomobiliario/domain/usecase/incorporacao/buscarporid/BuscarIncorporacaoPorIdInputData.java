package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.buscarporid;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuscarIncorporacaoPorIdInputData {
    private Long id;
}
