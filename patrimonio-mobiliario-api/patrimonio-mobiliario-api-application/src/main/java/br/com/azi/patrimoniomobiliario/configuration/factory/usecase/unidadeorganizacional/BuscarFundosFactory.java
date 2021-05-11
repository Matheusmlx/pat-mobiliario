package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.unidadeorganizacional;

import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarfundos.BuscarFundosUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarfundos.BuscarFundosUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarfundos.converter.BuscarFundosOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BuscarFundosFactory {

    @Autowired
    private SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration;

    @Bean("BuscarFundosUseCase")
    @DependsOn({"BuscarFundosOutputDataConverter"})
    public BuscarFundosUseCase createUseCase(BuscarFundosOutputDataConverter outputDataConverter) {
        return new BuscarFundosUseCaseImpl(
            sistemaDeGestaoAdministrativaIntegration,
            outputDataConverter
        );
    }

    @Bean("BuscarFundosOutputDataConverter")
    public BuscarFundosOutputDataConverter createConverter() {
        return new BuscarFundosOutputDataConverter();
    }
}
