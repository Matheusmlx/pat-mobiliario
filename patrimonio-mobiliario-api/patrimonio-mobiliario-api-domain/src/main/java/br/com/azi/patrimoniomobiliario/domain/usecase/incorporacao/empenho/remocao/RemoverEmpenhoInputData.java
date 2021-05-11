package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.remocao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RemoverEmpenhoInputData {
    private Long id;
}
