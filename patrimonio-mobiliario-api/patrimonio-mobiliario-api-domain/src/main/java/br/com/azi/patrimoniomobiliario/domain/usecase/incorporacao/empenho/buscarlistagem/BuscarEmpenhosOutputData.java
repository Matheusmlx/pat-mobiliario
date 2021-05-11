package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.buscarlistagem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuscarEmpenhosOutputData {
    private List<Item> items;
    private Long totalElements;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item {
        private Long id;
        private String numeroEmpenho;
        private LocalDateTime dataEmpenho;
        private BigDecimal valorEmpenho;
    }
}
