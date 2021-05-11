package br.com.azi.patrimoniomobiliario.gateway.integration.hal.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HalIntegrationProperties {
    private Hal hal;
    private HalConfig halConfig;
    private Usuario usuario;

    @Data
    @Builder
    public static class Hal {
        String repository;
        String url;
    }

    @Data
    @Builder
    public static class HalConfig {
        String url;
    }

    @Data
    @Builder
    public static class Usuario {
        private String login;
        private String senha;
    }

}
