package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.bucarpatrimoniosdevolucao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuscarPatrimoniosDevolucaoMovimentacaoTemporariaInputData {
    private Long movimentacaoId;
    private String conteudo;
    private Long page;
    private Long size;
    private String sort;
    private String direction;
}
