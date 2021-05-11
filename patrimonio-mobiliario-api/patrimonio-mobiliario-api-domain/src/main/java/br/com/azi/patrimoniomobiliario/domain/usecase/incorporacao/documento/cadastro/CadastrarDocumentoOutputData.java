package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.cadastro;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CadastrarDocumentoOutputData {
    private Long id;
    private String numero;
    private String data;
    private BigDecimal valor;
    private String uriAnexo;
    private Long tipo;
    private Long incorporacao;
}
