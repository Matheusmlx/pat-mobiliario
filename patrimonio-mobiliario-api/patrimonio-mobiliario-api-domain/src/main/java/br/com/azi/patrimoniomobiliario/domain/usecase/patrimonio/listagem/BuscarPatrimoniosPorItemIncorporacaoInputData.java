package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuscarPatrimoniosPorItemIncorporacaoInputData {
    private Long itemIncorporacaoId;
    private Long page;
    private Long size;
    private String sort;
    private String direction;
    private String situacao;
}
