package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.depreciacao;

import br.com.azi.patrimoniomobiliario.configuration.PatrimonioMobiliarioProperties;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeConfiguracoesIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.agendardepreciacao.AgendarDepreciacaoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.agendardepreciacao.AgendarDepreciacaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.agendardepreciacao.converter.AgendarDepreciacaoOutputDataConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.time.Clock;

@Configuration
@RequiredArgsConstructor
public class AgendarDepreciacaoFactory {

    private final PatrimonioMobiliarioProperties patrimonioMobiliarioProperties;

    private final SistemaDeConfiguracoesIntegration sistemaDeConfiguracoesIntegration;


    @Bean("AgendarDepreciacaoUseCase")
    @DependsOn({"AgendarDepreciacaoOutputDataConverter", "Clock"})
    public AgendarDepreciacaoUseCase createUseCase(AgendarDepreciacaoOutputDataConverter outputDataConverter, Clock clock) {
        return new AgendarDepreciacaoUseCaseImpl(
            sistemaDeConfiguracoesIntegration,
            outputDataConverter,
            patrimonioMobiliarioProperties.getDiaMensalDepreciacao(),
            patrimonioMobiliarioProperties.getFeriadosNacionais(),
            patrimonioMobiliarioProperties.getFeriadosLocais(),
            clock
        );
    }

    @Bean("AgendarDepreciacaoOutputDataConverter")
    public AgendarDepreciacaoOutputDataConverter createConverter() {
        return new AgendarDepreciacaoOutputDataConverter();
    }
}
