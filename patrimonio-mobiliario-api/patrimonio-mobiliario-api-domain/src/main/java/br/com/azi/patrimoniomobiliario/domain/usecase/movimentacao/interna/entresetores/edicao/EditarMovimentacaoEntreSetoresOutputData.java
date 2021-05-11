package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entresetores.edicao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditarMovimentacaoEntreSetoresOutputData {
    private Long id;
    private Situacao situacao;
    private Long orgao;
    private Long setorOrigem;
    private Long setorDestino;
    private Long responsavel;
    private String motivoObservacao;
    private String numeroNotaLancamentoContabil;
    private String dataNotaLancamentoContabil;
    private String numeroProcesso;

    public enum Situacao {
        EM_ELABORACAO,
        FINALIZADO
    }
}
