package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.intervalo.relatorios.termoresponsabilidade;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class GerarTermoGuardaResponsabilidadeReservaOutputData {
    private String nome;
    private String contentType;
    private byte[] content;
    private String uri;
    private String url;

    public GerarTermoGuardaResponsabilidadeReservaOutputData(String nome, String contentType, byte[] content, String uri, String url) {
        this.nome = nome;
        this.contentType = contentType;
        this.content = content.clone();
        this.uri = uri;
        this.url = url;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content.clone();
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
