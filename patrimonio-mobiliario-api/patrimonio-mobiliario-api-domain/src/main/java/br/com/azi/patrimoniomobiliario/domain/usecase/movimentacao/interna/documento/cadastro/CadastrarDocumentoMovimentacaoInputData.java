package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.cadastro;

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
public class CadastrarDocumentoMovimentacaoInputData {
    private String numero;
    private BigDecimal valor;
    private Date data;
    private String uriAnexo;
    private Long tipo;
    private Long movimentacao;
}
