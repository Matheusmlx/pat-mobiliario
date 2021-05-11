package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.configuracao.reserva.global;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.buscarproximonumero.BuscarProximoNumeroReservaPatrimonialUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.buscarproximonumero.BuscarProximoNumeroReservaPatrimonialUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.buscarproximonumero.converter.BuscarProximoNumeroReservaPatrimonialOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BuscarProximoNumeroReservaPatrimonialFactory {

    @Autowired
    private ReservaDataProvider reservaDataProvider;

    @Autowired
    private PatrimonioDataProvider patrimonioDataProvider;

    @Bean("BuscarProximoNumeroReservaPatrimonialUseCase")
    public BuscarProximoNumeroReservaPatrimonialUseCase createUseCase(BuscarProximoNumeroReservaPatrimonialOutputDataConverter outputDataConverter) {
        return new BuscarProximoNumeroReservaPatrimonialUseCaseImpl(
                reservaDataProvider,
                patrimonioDataProvider,
                outputDataConverter
        );
    }

    @Bean("BuscarProximoNumeroReservaPatrimonialOutputDataConverter")
    public BuscarProximoNumeroReservaPatrimonialOutputDataConverter createOutputDataConverter() {
        return new BuscarProximoNumeroReservaPatrimonialOutputDataConverter();
    }

}
