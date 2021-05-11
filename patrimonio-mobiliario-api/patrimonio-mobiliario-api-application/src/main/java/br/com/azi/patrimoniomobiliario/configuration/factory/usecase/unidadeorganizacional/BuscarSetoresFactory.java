package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.unidadeorganizacional;

import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarsetores.BuscarSetoresUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarsetores.BuscarSetoresUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarsetores.converter.BuscarSetoresOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BuscarSetoresFactory {

    @Autowired
    private SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration;

    @Bean("BuscarSetoresUseCase")
    @DependsOn({"BuscarSetoresOutputDataConverter"})
    public BuscarSetoresUseCase createUseCase(BuscarSetoresOutputDataConverter outputDataConverter) {
        return new BuscarSetoresUseCaseImpl(
            sistemaDeGestaoAdministrativaIntegration,
            outputDataConverter);
    }

    @Bean("BuscarSetoresOutputDataConverter")
    public BuscarSetoresOutputDataConverter createConverter() {
        return new BuscarSetoresOutputDataConverter();
    }
}
