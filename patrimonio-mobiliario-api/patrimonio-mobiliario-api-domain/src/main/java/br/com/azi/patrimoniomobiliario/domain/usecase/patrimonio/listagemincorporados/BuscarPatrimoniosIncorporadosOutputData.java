package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemincorporados;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuscarPatrimoniosIncorporadosOutputData {
    private List<Item> items;
    private Long totalPages;
    private Long totalElements;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item {
        private Long id;
        private String descricao;
        private String orgao;
        private String setor;
        private String situacao;
    }
}
