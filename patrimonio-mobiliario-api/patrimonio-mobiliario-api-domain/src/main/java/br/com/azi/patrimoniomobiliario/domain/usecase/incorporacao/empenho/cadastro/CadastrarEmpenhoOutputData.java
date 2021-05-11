package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.cadastro;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CadastrarEmpenhoOutputData {
    private Long id;
    private String numeroEmpenho;
    private LocalDateTime dataEmpenho;
    private BigDecimal valorEmpenho;
    private Long incorporacaoId;
}
