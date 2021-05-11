package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.configuracao.reserva.global.intervalo;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.existeporsituacao.ExisteIntervalosPorSituacaoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.existeporsituacao.ExisteIntervalosPorSituacaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.existeporsituacao.converter.ExisteIntervalosPorSituacaoOutputDataConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ExisteIntervalosPorSituacaoFactory {

    private final ReservaIntervaloDataProvider reservaIntervaloDataProvider;

    @Bean("ExisteIntervalosPorSituacaoUseCase")
    public ExisteIntervalosPorSituacaoUseCase createUseCase(ExisteIntervalosPorSituacaoOutputDataConverter outputDataConverter) {
        return new ExisteIntervalosPorSituacaoUseCaseImpl(
            reservaIntervaloDataProvider,
            outputDataConverter
        );
    }

    @Bean("ExisteIntervalosPorSituacaoOutputDataConverter")
    public ExisteIntervalosPorSituacaoOutputDataConverter createOutputDataConverter() {
        return new ExisteIntervalosPorSituacaoOutputDataConverter();
    }
}
