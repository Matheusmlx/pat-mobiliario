package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.buscar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuscarContasContabeisOutputData {

    List<Item> items;
    Long totalPages;
    Long totalElements;

    @Data
    @NoArgsConstructor
    public static class Item{
        private Long id;
        private String codigo;
        private String descricao;
        private String tipo;
        private String situacao;
        private String tipoBem;
        private Integer vidaUtil;
        private BigDecimal percentualResidual;
    }
}
