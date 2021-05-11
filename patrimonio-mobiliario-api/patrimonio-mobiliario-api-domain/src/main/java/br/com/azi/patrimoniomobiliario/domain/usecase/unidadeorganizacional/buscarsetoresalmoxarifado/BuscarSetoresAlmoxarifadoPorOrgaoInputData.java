package br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarsetoresalmoxarifado;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuscarSetoresAlmoxarifadoPorOrgaoInputData {
    private Long idOrgao;
}
