package br.com.azi.patrimoniomobiliario.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Empenho {
    private Long id;
    private String numeroEmpenho;
    private LocalDateTime dataEmpenho;
    private BigDecimal valorEmpenho;
    private Long incorporacaoId;

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Filtro extends FiltroBase {
        private Long incorporacaoId;
    }
}
