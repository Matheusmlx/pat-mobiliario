package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.configuracao.reserva.global;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloNumeroDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.finalizar.FinalizarReservaPatrimonialUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.finalizar.FinalizarReservaPatrimonialUseCaseImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FinalizarReservaPatrimonialFactory {

    @Autowired
    private ReservaDataProvider reservaDataProvider;

    @Autowired
    private ReservaIntervaloDataProvider reservaIntervaloDataProvider;

    @Autowired
    private ReservaIntervaloNumeroDataProvider reservaIntervaloNumeroDataProvider;

    @Autowired
    private SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    @Bean("FinalizarReservaPatrimonialUseCase")
    public FinalizarReservaPatrimonialUseCase createUseCase() {
        return new FinalizarReservaPatrimonialUseCaseImpl(
                reservaDataProvider,
                reservaIntervaloDataProvider,
                reservaIntervaloNumeroDataProvider,
                sessaoUsuarioDataProvider
        );
    }

}
