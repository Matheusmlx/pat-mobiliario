package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.configuracao.reserva.global.intervalo;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.validarintervalo.ValidarIntervaloUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.validarintervalo.ValidarIntervaloUseCaseImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidarIntervaloFactory {

    @Autowired
    private ReservaDataProvider reservaDataProvider;

    @Autowired
    private ReservaIntervaloDataProvider reservaIntervaloDataProvider;

    @Bean("ValidarIntervaloUseCase")
    public ValidarIntervaloUseCase createUseCase() {
        return new ValidarIntervaloUseCaseImpl(
                reservaDataProvider,
                reservaIntervaloDataProvider);
    }

}
