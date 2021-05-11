package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.buscarporid;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuscarPatrimonioPorIdInputData {
    private Long id;
}
