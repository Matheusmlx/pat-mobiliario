package br.com.azi.patrimoniomobiliario.gateway.integration.efornecedor.usecase.buscarporid.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuscarFornecedorPorIdIntegrationResponse {
    private Long id;
    private String razaoSocial;
    private String cpfCnpj;
}
