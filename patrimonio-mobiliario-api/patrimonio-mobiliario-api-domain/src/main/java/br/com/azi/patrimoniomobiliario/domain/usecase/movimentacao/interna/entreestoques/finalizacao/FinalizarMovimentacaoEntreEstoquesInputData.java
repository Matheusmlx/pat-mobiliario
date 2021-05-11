package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.finalizacao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinalizarMovimentacaoEntreEstoquesInputData {
    private Long id;
}
