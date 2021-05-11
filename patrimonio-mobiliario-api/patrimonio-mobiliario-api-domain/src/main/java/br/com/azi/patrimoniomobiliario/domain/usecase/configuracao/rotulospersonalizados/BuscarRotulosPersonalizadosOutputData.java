package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.rotulospersonalizados;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuscarRotulosPersonalizadosOutputData {
    Map<String, Object> rotulosPersonalizados;
}
