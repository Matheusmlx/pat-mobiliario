package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.unidadeorganizacional;

import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarorgao.BuscarUnidadesOrganizacionaisUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarorgao.BuscarUnidadesOrganizacionaisUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarorgao.converter.BuscarUnidadesOrganizacionaisOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BuscarUnidadesOrganizacionaisFactory {

    @Autowired
    private SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration;

    @Bean("BuscarUnidadesOrganizacionaisUseCase")
    @DependsOn({"BuscarUnidadesOrganizacionaisOutputDataConverter"})
    public BuscarUnidadesOrganizacionaisUseCase createUseCase(BuscarUnidadesOrganizacionaisOutputDataConverter outputDataConverter) {
        return new BuscarUnidadesOrganizacionaisUseCaseImpl(
            sistemaDeGestaoAdministrativaIntegration,
            outputDataConverter);
    }

    @Bean("BuscarUnidadesOrganizacionaisOutputDataConverter")
    public BuscarUnidadesOrganizacionaisOutputDataConverter createConverter() {
        return new BuscarUnidadesOrganizacionaisOutputDataConverter();
    }
}
