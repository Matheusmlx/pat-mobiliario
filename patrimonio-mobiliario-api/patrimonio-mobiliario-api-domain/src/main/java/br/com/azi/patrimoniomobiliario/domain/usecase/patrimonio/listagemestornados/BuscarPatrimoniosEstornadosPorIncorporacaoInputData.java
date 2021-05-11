package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemestornados;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuscarPatrimoniosEstornadosPorIncorporacaoInputData {
    private Long incorporacaoId;
    private Long page;
    private Long size;
    private String sort;
    private String direction;
}
