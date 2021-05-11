package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.buscar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuscarDocumentosInputData {
    private Long incorporacao;
}
