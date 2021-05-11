package br.com.azi.patrimoniomobiliario.domain.usecase.naturezadespesa.buscar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuscarNaturezasDespesaInputData {
    private String itemCatalogoCodigo;
}
