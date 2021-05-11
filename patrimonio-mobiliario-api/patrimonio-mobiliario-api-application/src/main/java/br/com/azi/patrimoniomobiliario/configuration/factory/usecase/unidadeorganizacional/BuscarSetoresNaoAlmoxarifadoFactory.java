package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.unidadeorganizacional;

import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarsetoresnaoalmoxarifado.BuscarSetoresNaoAlmoxarifadoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarsetoresnaoalmoxarifado.BuscarSetoresNaoAlmoxarifadoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarsetoresnaoalmoxarifado.converter.BuscarSetoresNaoAlmoxarifadoOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BuscarSetoresNaoAlmoxarifadoFactory {

    @Autowired
    private SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration;

    @Bean("BuscarSetoresNaoAlmoxarifadoUseCase")
    @DependsOn({"BuscarSetoresNaoAlmoxarifadoOutputDataConverter"})
    public BuscarSetoresNaoAlmoxarifadoUseCase createUseCase(BuscarSetoresNaoAlmoxarifadoOutputDataConverter converter) {
        return new BuscarSetoresNaoAlmoxarifadoUseCaseImpl(
            sistemaDeGestaoAdministrativaIntegration,
            converter
        );
    }

    @Bean("BuscarSetoresNaoAlmoxarifadoOutputDataConverter")
    public BuscarSetoresNaoAlmoxarifadoOutputDataConverter createConverver() {
        return new BuscarSetoresNaoAlmoxarifadoOutputDataConverter();
    }
}
