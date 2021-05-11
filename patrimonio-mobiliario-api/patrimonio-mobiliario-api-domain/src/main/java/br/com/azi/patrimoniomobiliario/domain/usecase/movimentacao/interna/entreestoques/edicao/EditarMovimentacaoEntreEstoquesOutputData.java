package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.edicao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditarMovimentacaoEntreEstoquesOutputData {
    private Long id;
    private String situacao;
    private Long orgao;
    private Long setorOrigem;
    private Long setorDestino;
    private Long responsavel;
    private String motivoObservacao;
    private String numeroNotaLancamentoContabil;
    private String dataNotaLancamentoContabil;
    private String numeroProcesso;
}
