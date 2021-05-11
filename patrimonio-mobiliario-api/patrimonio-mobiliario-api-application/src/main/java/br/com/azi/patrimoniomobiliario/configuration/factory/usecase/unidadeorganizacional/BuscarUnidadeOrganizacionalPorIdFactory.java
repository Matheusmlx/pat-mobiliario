package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.unidadeorganizacional;

import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarsetoresalmoxarifado.BuscarSetoresAlmoxarifadoPorOrgaoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarsetoresalmoxarifado.BuscarSetoresAlmoxarifadoPorOrgaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarsetoresalmoxarifado.converter.BuscarSetoresAlmoxarifadoPorOrgaoOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BuscarUnidadeOrganizacionalPorIdFactory {

    @Autowired
    private SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration;

    @Bean("BuscarUnidadeOrganizacionalPorIdUseCase")
    @DependsOn({"BuscarUnidadeOrganizacionalPorIdOutputDataConverter"})
    public BuscarSetoresAlmoxarifadoPorOrgaoUseCase createUseCase(BuscarSetoresAlmoxarifadoPorOrgaoOutputDataConverter outputDataConverter) {
        return new BuscarSetoresAlmoxarifadoPorOrgaoUseCaseImpl(
            sistemaDeGestaoAdministrativaIntegration,
            outputDataConverter
        );
    }

    @Bean("BuscarUnidadeOrganizacionalPorIdOutputDataConverter")
    public BuscarSetoresAlmoxarifadoPorOrgaoOutputDataConverter createConverter() {
        return new BuscarSetoresAlmoxarifadoPorOrgaoOutputDataConverter();
    }
}
