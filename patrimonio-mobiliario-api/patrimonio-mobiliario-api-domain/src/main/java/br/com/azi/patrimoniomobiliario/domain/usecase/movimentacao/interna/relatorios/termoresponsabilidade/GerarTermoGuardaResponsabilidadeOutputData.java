package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.relatorios.termoresponsabilidade;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GerarTermoGuardaResponsabilidadeOutputData {
    private String nome;
    private String contentType;
    private byte[] content;
    private String uri;
    private String url;
}
