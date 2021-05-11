package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.buscarlistagem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuscarReconhecimentosInputData {
    private Long page;
    private Long size;
    private String sort;
    private String direction;
    private String conteudo;
    private String situacao;
}
