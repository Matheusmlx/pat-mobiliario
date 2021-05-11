package br.com.azi.patrimoniomobiliario.gateway.integration;


import br.com.azi.patrimoniomobiliario.domain.entity.SessaoUsuario;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeConfiguracoesIntegration;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.entity.HalIntegrationProperties;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.alterarpropriedade.AlterarPropriedadeIntegrationUseCase;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.autenticar.AutenticarIntegrationUseCase;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SistemaDeConfiguracoesIntegrationHalImpl implements SistemaDeConfiguracoesIntegration {

    private final HalIntegrationProperties integrationProperties;

    private final AutenticarIntegrationUseCase autenticarIntegrationUseCase;

    private final AlterarPropriedadeIntegrationUseCase alterarPropriedadeIntegrationUseCase;

    @Override
    public void alterarPropriedade(String nome, String valor) {
        SessaoUsuario sessaoUsuario = autenticarIntegrationUseCase.executar(integrationProperties);
        alterarPropriedadeIntegrationUseCase.executar(integrationProperties, sessaoUsuario, nome, valor);
    }
}
