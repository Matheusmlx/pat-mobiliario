package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.buscarporid;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuscarConvenioPorIdInputData {
    private Long id;
}
