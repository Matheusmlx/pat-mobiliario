package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.buscar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuscarContasContabeisInputData {
    private Long page;
    private Long size;
    private String sort;
    private String direction;
    private String conteudo;
    private Long produtoId;

}
