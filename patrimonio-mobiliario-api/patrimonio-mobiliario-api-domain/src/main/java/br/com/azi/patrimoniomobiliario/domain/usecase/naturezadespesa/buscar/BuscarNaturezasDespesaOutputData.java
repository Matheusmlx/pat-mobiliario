package br.com.azi.patrimoniomobiliario.domain.usecase.naturezadespesa.buscar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuscarNaturezasDespesaOutputData {

    List<NaturezaDespesa> items;

    @Data
    @NoArgsConstructor
    public static class NaturezaDespesa{
        private Long id;
        private String descricao;
        private String despesa;
    }
}
