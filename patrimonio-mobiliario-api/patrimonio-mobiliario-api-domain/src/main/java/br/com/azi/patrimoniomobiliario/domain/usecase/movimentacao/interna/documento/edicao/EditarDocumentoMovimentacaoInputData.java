package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.edicao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditarDocumentoMovimentacaoInputData {
    private Long id;
    private String numero;
    private Date data;
    private BigDecimal valor;
    private String uriAnexo;
    private Long movimentacao;
    private Long tipo;
}
