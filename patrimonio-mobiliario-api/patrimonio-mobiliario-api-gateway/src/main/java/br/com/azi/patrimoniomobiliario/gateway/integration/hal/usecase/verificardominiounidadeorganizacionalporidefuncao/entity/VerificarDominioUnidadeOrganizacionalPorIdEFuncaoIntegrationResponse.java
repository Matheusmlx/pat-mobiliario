package br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.verificardominiounidadeorganizacionalporidefuncao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerificarDominioUnidadeOrganizacionalPorIdEFuncaoIntegrationResponse {

    private Boolean possuiDominio;
}
