package br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.listagem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuscarDepreciacoesInputData {
    private Long id;
}
