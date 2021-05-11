package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.buscarporid;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuscarMovimentacaoPorIdOutputData {
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
    private String dataDevolucao;
    private String tipo;

    public enum Situacao {
        AGUARDANDO_DEVOLUCAO,
        DEVOLVIDO_PARCIAL,
        DEVOLVIDO,
        EM_ELABORACAO,
        EM_PROCESSAMENTO,
        ERRO_PROCESSAMENTO,
        FINALIZADO
    }
}
