package br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.listagem;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class BuscarDepreciacoesOutputData {

    List<Depreciacao> items;

    @Data
    @NoArgsConstructor
    public static class Depreciacao {
        private Long id;
        private LocalDateTime dataInicial;
        private LocalDateTime dataFinal;
        private String mesReferencia;
        private BigDecimal valorAnterior;
        private BigDecimal valorPosterior;
        private BigDecimal valorSubtraido;
        private BigDecimal taxaAplicada;
        private String orgaoSigla;
        private String setorSigla;
    }
}
