package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.relatorios.memorando.memorandomovimentacaoemelaboracao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GerarMemorandoMovimentacaoInternaEmElaboracaoOutputData {
    private String nome;
    private String contentType;
    private byte[] content;
    private String uri;
    private String url;
}
