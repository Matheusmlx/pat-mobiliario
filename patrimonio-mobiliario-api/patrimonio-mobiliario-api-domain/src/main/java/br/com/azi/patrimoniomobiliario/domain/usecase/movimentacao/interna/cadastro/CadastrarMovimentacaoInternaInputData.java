package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.cadastro;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CadastrarMovimentacaoInternaInputData {
    private String tipo;
}
