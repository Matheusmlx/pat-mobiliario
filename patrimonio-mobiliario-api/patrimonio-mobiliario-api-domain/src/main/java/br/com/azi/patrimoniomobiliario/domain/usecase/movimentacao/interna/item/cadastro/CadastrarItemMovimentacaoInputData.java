package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.cadastro;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CadastrarItemMovimentacaoInputData {
    private Long movimentacaoId;
    private List<Long> patrimoniosId;
    private List<Long> patrimoniosNaoConsiderar;
    private boolean todosSelecionados;
}
