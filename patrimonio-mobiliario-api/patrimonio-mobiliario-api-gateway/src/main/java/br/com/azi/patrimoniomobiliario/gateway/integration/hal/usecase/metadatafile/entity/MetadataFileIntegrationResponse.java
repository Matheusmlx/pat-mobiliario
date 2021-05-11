package br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.metadatafile.entity;

import lombok.Data;

@Data
public class MetadataFileIntegrationResponse {
    private String uri;
    private String nome;
    private String extensao;
    private String hash;
    private Boolean removido;
}
