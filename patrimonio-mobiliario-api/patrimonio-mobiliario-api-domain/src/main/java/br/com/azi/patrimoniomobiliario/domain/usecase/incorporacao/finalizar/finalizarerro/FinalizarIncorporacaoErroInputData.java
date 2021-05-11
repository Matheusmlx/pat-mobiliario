package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizarerro;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinalizarIncorporacaoErroInputData {
    private Long incorporacaoId;
    private String erroProcessamento;
}
