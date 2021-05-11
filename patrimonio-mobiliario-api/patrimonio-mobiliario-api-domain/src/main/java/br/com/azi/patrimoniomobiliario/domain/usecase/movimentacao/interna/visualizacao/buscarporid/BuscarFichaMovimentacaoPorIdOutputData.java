package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.visualizacao.buscarporid;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuscarFichaMovimentacaoPorIdOutputData {
    private Long id;
    private String situacao;
    private String tipo;
    private String codigo;
    private String motivoObservacao;
    private String numeroProcesso;
    private String usuarioCriacao;
    private String orgao;
    private String setorOrigem;
    private String setorDestino;
    private String responsavel;
    private String numeroNotaLancamentoContabil;
    private String dataNotaLancamentoContabil;
    private String dataFinalizacao;
    private String dataDevolucao;
    private String dataEnvio;
}
