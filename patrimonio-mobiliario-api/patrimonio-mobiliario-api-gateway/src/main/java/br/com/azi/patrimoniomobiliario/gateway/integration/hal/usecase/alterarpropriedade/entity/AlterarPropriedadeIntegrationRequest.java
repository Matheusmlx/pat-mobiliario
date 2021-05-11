package br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.alterarpropriedade.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlterarPropriedadeIntegrationRequest {
    private String key;
    private String value;
}
