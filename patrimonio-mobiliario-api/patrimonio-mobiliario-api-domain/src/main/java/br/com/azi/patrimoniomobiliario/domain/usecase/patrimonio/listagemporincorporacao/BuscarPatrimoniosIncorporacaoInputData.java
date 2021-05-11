package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemporincorporacao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuscarPatrimoniosIncorporacaoInputData {
    private Long incorporacaoId;
    private Long page;
    private Long size;
    private String sort;
    private String direction;
    private String conteudo;
}
