package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.exclusao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExcluirItemIncorporacaoInputData {
    private Long id;
    private Long idIncorporacao;
}
