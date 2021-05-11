package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.configuracao.reserva.global.intervalo;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.buscarintervalodisponivel.BuscarIntervaloDisponivelUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.buscarintervalodisponivel.BuscarIntervaloDisponivelUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.buscarintervalodisponivel.converter.BuscarIntervaloDisponivelOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BuscarIntervaloDisponivelFactory {

    @Autowired
    private ReservaDataProvider reservaDataProvider;

    @Autowired
    private ReservaIntervaloDataProvider reservaIntervaloDataProvider;

    @Bean("BuscarIntervaloReservaPatrimonialUseCase")
    @DependsOn({"BuscarIntervaloReservaPatrimonialOutputDataConverter"})
    public BuscarIntervaloDisponivelUseCase createUseCase(BuscarIntervaloDisponivelOutputDataConverter outputDataConverter) {
        return new BuscarIntervaloDisponivelUseCaseImpl(
            reservaDataProvider,
            reservaIntervaloDataProvider,
            outputDataConverter
        );
    }

    @Bean("BuscarIntervaloReservaPatrimonialOutputDataConverter")
    public BuscarIntervaloDisponivelOutputDataConverter createOutpuDataConverter() {
        return new BuscarIntervaloDisponivelOutputDataConverter();
    }

}
