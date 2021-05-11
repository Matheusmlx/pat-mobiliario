package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.edicao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditarDistribuicaoInputData {
    private Long id;
    private Long orgao;
    private Long setorOrigem;
    private Long setorDestino;
    private Long responsavel;
    private String motivoObservacao;
    private String numeroNotaLancamentoContabil;
    private Date dataNotaLancamentoContabil;
    private String numeroProcesso;
}
