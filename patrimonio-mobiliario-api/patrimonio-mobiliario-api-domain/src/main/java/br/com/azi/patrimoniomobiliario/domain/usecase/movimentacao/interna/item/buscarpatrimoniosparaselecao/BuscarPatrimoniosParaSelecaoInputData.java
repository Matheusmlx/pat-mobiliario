package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.buscarpatrimoniosparaselecao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuscarPatrimoniosParaSelecaoInputData {
    private Long movimentacaoId;
    private String conteudo;
    private Long page;
    private Long size;
    private String sort;
    private String direction;
}
