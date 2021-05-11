package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.estadoconservacao.listagem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuscarEstadosConservacaoOutputData {
    private List<Item> estadosConservacao;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item {
        Long id;
        String nome;
        Integer prioridade;
    }
}
