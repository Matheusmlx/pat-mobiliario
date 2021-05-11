package br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarfundos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuscarFundosInputData {
    private Long idEstruturaAdministradora;
}
