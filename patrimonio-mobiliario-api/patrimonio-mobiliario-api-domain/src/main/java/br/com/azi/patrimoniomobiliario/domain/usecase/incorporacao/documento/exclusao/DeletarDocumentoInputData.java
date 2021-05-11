package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.exclusao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeletarDocumentoInputData {
    private Long id;
    private Long incorporacao;
}
