package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.configuracao.reserva.global.intervalo;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.remover.RemoverReservaIntervaloUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.remover.RemoverReservaIntervaloUseCaseImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RemoverReservaIntervaloFactory {

    @Autowired
    private ReservaIntervaloDataProvider reservaIntervaloDataProvider;

    @Autowired
    private ReservaDataProvider reservaDataProvider;

    @Autowired
    private SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    @Bean("RemoverReservaIntervaloUseCase")
    public RemoverReservaIntervaloUseCase createUseCase() {
        return new RemoverReservaIntervaloUseCaseImpl(
                reservaIntervaloDataProvider,
                reservaDataProvider,
                sessaoUsuarioDataProvider
        );
    }

}
