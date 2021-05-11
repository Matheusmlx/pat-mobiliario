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
public class EditarContaContabilOutputData {

    private Long id;
    private Long contaContabil;
    private String metodo;
    private String tipo;
    private String tipoBem;
    private Integer vidaUtil;
    private BigDecimal percentualResidual;
}
