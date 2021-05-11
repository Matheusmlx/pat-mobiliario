package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.buscarsituacao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuscarSituacaoIncorporacaoInputData {
    private Long incorporacaoId;
}
