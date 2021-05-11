package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entresetores.finalizacao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinalizarMovimentacaoEntreSetoresInputData {
    private Long id;
}
