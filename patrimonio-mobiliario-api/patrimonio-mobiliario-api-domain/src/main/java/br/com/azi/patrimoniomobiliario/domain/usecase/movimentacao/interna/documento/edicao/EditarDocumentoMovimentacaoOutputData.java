package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.edicao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditarDocumentoMovimentacaoOutputData {
    private Long id;
    private String numero;
    private String data;
    private BigDecimal valor;
    private String uriAnexo;
    private Long tipo;
    private Long movimentacao;
}
