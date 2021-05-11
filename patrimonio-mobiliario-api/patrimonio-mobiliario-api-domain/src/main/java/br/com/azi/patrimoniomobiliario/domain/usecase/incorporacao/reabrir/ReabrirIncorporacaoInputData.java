package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.reabrir;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReabrirIncorporacaoInputData {
    private Long incorporacaoId;
}
