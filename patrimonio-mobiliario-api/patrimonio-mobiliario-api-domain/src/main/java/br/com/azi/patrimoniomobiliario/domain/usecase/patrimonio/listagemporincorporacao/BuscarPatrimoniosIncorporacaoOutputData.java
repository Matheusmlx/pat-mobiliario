package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemporincorporacao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuscarPatrimoniosIncorporacaoOutputData {

    private List<Item> items;
    private Long totalPages;
    private Long totalElements;
    private Long totalElementsOfAllPages;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item {
        private Long id;
        private String numero;
        private String descricao;
        private BigDecimal valor;
    }

}
