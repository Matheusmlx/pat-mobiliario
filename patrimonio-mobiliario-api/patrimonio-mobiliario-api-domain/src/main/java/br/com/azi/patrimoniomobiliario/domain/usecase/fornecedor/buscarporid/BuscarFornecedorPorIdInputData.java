package br.com.azi.patrimoniomobiliario.domain.usecase.fornecedor.buscarporid;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuscarFornecedorPorIdInputData {
    private Long id;
}
