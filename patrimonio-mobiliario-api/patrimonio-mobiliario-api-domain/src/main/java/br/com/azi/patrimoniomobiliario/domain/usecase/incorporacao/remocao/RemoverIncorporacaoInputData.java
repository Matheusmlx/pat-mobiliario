package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.remocao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RemoverIncorporacaoInputData {
    private Long id;
}
