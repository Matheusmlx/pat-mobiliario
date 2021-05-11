package br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarorgaoporfiltro;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuscarOrgaoPorFiltroInputData {
    private String conteudo;
    private Long page;
    private Long size;
}
