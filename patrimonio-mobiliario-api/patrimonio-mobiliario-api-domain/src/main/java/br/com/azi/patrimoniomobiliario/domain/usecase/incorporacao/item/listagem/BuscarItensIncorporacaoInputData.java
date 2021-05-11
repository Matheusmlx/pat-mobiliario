package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.listagem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuscarItensIncorporacaoInputData {
    private Long incorporacaoId;
    private Long page;
    private Long size;
    private String sort;
    private String direction;
    private String conteudo;
    private String situacao;
}
