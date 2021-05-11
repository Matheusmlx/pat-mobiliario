package br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.uploadfile.entity;

import lombok.Data;

@Data
public class UploadFileIntegrationResponse {
    private String name;
    private String uri;
    private String url;
}
