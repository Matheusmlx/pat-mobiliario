package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.visualizar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisualizarIncorporacaoOutputData {
    private Long id;
    private String codigo;
    private String fornecedor;
    private String dataRecebimento;
    private String situacao;
    private String reconhecimento;
    private String numProcesso;
    private String orgao;
    private String setor;
    private String nota;
    private String fundo;
    private BigDecimal valorNota;
    private String dataNota;
    private String convenio;
    private String projeto;
    private String comodante;
    private String dataFinalizacao;
    private String numeroNotaLancamentoContabil;
    private String dataNotaLancamentoContabil;
    private Boolean possuiPatrimoniosEmOutrasMovimentacoes;
}
