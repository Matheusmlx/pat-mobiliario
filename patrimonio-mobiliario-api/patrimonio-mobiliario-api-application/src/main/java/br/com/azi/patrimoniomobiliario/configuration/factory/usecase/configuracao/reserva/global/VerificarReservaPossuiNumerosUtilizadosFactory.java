package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.configuracao.reserva.global;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloNumeroDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.possuinumerosutilizados.VerificarReservaPossuiNumerosUtilizadosUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.possuinumerosutilizados.VerificarReservaPossuiNumerosUtilizadosUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.possuinumerosutilizados.converter.VerificarReservaPossuiNumerosUtilizadosOutputDataConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class VerificarReservaPossuiNumerosUtilizadosFactory {

    private final ReservaDataProvider reservaDataProvider;

    private final ReservaIntervaloDataProvider reservaIntervaloDataProvider;

    private final ReservaIntervaloNumeroDataProvider reservaIntervaloNumeroDataProvider;

    @Bean("VerificarReservaPossuiNumerosUtilizadosUseCase")
    public VerificarReservaPossuiNumerosUtilizadosUseCase createUseCase(VerificarReservaPossuiNumerosUtilizadosOutputDataConverter converter) {
        return new VerificarReservaPossuiNumerosUtilizadosUseCaseImpl(
            reservaDataProvider,
            reservaIntervaloDataProvider,
            reservaIntervaloNumeroDataProvider,
            converter
        );
    }

    @Bean("VerificarReservaPossuiNumerosUtilizadosOutputDataConverter")
    public VerificarReservaPossuiNumerosUtilizadosOutputDataConverter createOutputDataConverter() {
        return new VerificarReservaPossuiNumerosUtilizadosOutputDataConverter();
    }

}
