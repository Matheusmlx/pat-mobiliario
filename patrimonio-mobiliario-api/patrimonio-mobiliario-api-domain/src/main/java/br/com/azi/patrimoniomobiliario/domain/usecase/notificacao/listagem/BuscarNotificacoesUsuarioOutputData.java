package br.com.azi.patrimoniomobiliario.domain.usecase.notificacao.listagem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuscarNotificacoesUsuarioOutputData {

    private List<Item> items;
    private Long totalPages;
    private Long totalElements;
    private Long quantidadeNaoVisualizadas;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item {
        private Long id;
        private String origem;
        private Long origemId;
        private String assunto;
        private String mensagem;
        private String dataCriacao;
        private Boolean visualizada;
    }
}
