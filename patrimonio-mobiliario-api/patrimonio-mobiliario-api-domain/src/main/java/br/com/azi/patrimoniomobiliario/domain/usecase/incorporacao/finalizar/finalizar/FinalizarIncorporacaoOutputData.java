package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinalizarIncorporacaoOutputData {
    private Long id;
    private String codigo;
    private String situacao;
}
