package br.com.azi.patrimoniomobiliario.domain.usecase.catalogo.buscarlistagem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuscarItensCatalogoInputData {
    private Long page;
    private Long size;
    private String sort;
    private String direction;
    private String conteudo;
}
