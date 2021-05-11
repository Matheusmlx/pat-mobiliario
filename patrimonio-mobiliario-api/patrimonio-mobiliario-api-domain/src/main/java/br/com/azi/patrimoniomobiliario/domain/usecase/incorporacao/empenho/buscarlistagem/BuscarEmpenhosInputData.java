package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.buscarlistagem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuscarEmpenhosInputData {
    private Long incorporacaoId;
}
