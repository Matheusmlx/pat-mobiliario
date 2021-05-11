package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.configuracao.reserva.global.intervalo;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.buscarproximonumero.BuscarProximoNumeroReservaIntervaloUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.buscarproximonumero.BuscarProximoNumeroReservaIntervaloUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.buscarproximonumero.converter.BuscarProximoNumeroReservaIntervaloOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BuscarProximoNumeroReservaIntervaloFactory {

    @Autowired
    private ReservaDataProvider reservaDataProvider;

    @Autowired
    private ReservaIntervaloDataProvider reservaIntervaloDataProvider;

    @Bean("BuscarProximoNumeroReservaIntervaloUseCase")
    public BuscarProximoNumeroReservaIntervaloUseCase createUseCase(BuscarProximoNumeroReservaIntervaloOutputDataConverter outputDataConverter) {
        return new BuscarProximoNumeroReservaIntervaloUseCaseImpl(
                reservaIntervaloDataProvider,
                reservaDataProvider,
                outputDataConverter
        );
    }

    @Bean("BuscarProximoNumeroReservaIntervaloOutputDataConverter")
    public BuscarProximoNumeroReservaIntervaloOutputDataConverter createOutputDataConverter() {
        return new BuscarProximoNumeroReservaIntervaloOutputDataConverter();
    }

}
