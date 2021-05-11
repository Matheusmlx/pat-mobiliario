package br.com.azi.patrimoniomobiliario.configuration.factory.gateway.integration;

import br.com.azi.patrimoniomobiliario.configuration.PatrimonioMobiliarioProperties;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeConfiguracoesIntegration;
import br.com.azi.patrimoniomobiliario.gateway.integration.SistemaDeConfiguracoesIntegrationHalImpl;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.entity.HalIntegrationProperties;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.alterarpropriedade.AlterarPropriedadeIntegrationUseCase;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.autenticar.AutenticarIntegrationUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@RefreshScope
@Configuration
public class SistemaDeConfiguracoesIntegrationHalFactory {

    @Autowired
    private PatrimonioMobiliarioProperties patrimonioMobiliarioProperties;

    @Autowired
    private AlterarPropriedadeIntegrationUseCase alterarPropriedadeIntegrationUseCase;

    @Autowired
    private AutenticarIntegrationUseCase autenticarIntegrationUseCase;

    @Bean("SistemaDeConfiguracoesIntegration")
    @Primary
    public SistemaDeConfiguracoesIntegration createBean() {
        HalIntegrationProperties halProperties = HalIntegrationProperties
                .builder()
                .hal(
                        HalIntegrationProperties.Hal
                                .builder()
                                .repository(patrimonioMobiliarioProperties.getIntegracao().getHal().getRepository())
                                .url(patrimonioMobiliarioProperties.getIntegracao().getHal().getUrl())
                                .build()
                )
                .halConfig(
                        HalIntegrationProperties.HalConfig
                                .builder()
                                .url(patrimonioMobiliarioProperties.getIntegracao().getHalConfig().getUrl())
                                .build()
                )
                .usuario(
                        HalIntegrationProperties.Usuario
                                .builder()
                                .login(patrimonioMobiliarioProperties.getIntegracao().getUsuario().getLogin())
                                .senha(patrimonioMobiliarioProperties.getIntegracao().getUsuario().getSenha())
                                .build()
                )
                .build();

        return new SistemaDeConfiguracoesIntegrationHalImpl(
                halProperties,
                autenticarIntegrationUseCase,
                alterarPropriedadeIntegrationUseCase
        );
    }
}
