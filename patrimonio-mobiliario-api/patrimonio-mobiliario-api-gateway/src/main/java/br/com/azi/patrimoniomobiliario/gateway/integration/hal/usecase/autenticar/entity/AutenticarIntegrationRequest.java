package br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.autenticar.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AutenticarIntegrationRequest {
    private String username;
    private String password;
    private String uriRedirectAfterLogin;
    private String tipo;
    private String produto;
}
