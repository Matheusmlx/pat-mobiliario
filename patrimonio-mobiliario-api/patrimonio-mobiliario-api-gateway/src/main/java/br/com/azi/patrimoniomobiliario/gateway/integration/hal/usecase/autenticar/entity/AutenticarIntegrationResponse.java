package br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.autenticar.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutenticarIntegrationResponse {

    List<EstruturaOrganizacional> content;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class EstruturaOrganizacional {
        private Long id;
        private String sigla;
        private String nome;
        private String codigoHierarquia;
    }
}
