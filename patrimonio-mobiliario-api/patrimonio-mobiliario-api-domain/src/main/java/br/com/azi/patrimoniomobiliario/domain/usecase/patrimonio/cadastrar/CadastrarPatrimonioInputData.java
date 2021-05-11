package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.cadastrar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CadastrarPatrimonioInputData {
    private Long quantidade;
    private BigDecimal valorTotal;
    private BigDecimal valorTotalAnterior;
    private Long contaContabilId;
    private Long itemIncorporacaoId;
}
