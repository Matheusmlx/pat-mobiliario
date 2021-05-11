package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.dadosdepreciacao;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EditarContaContabilInputData {

    private Long id;
    private Long contaContabil;
    private String tipo;
    private String tipoBem;
    private Integer vidaUtil;
    private BigDecimal percentualResidual;
    private BigDecimal taxa;
}
