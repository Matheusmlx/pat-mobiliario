package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.configuracao.reserva.intervalo.relatorios.termoresponsabilidade;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeRelatoriosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.intervalo.relatorios.termoresponsabilidade.GerarTermoGuardaResponsabilidadeReservaUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.intervalo.relatorios.termoresponsabilidade.GerarTermoGuardaResponsabilidadeReservaUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.intervalo.relatorios.termoresponsabilidade.converter.GerarTermoGuardaResponsabilidadeReservaOutputDataConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.time.Clock;

@Configuration
@RequiredArgsConstructor
public class GerarTermoGuardaResponsabilidadeReservaPatrimonialFactory {

    private final ReservaDataProvider reservaDataProvider;

    private final ReservaIntervaloDataProvider reservaIntervaloDataProvider;

    private final SistemaDeRelatoriosIntegration sistemaDeRelatoriosIntegration;

    @Bean("GerarTermoGuardaResponsabilidadeReservaUseCase")
    @DependsOn({"GerarTermoGuardaResponsabilidadeReservaOutputDataConverter", "Clock"})
    public GerarTermoGuardaResponsabilidadeReservaUseCase createUseCase(GerarTermoGuardaResponsabilidadeReservaOutputDataConverter outputDataConverter,
                                                                        Clock clock) {
        return new GerarTermoGuardaResponsabilidadeReservaUseCaseImpl(
            clock,
            reservaDataProvider,
            reservaIntervaloDataProvider,
            sistemaDeRelatoriosIntegration,
            outputDataConverter
        );
    }

    @Bean("GerarTermoGuardaResponsabilidadeReservaOutputDataConverter")
    public GerarTermoGuardaResponsabilidadeReservaOutputDataConverter createOutputDataConverter() {
        return new GerarTermoGuardaResponsabilidadeReservaOutputDataConverter();
    }

}
