package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.configuracao.reserva.global;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.buscarporid.BuscarReservaPorIdUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.buscarporid.BuscarReservaPorIdUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.buscarporid.converter.BuscarReservaPorIdOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BuscarReservaPorIdFactory {

    @Autowired
    private ReservaDataProvider reservaDataProvider;

    @Autowired
    private SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    @Bean("BuscarReservaPorIdUseCase")
    @DependsOn({"BuscarReservaPorIdOutputDataConverter"})
    public BuscarReservaPorIdUseCase createUseCase(BuscarReservaPorIdOutputDataConverter outputDataConverter) {
        return new BuscarReservaPorIdUseCaseImpl(
            reservaDataProvider,
            sessaoUsuarioDataProvider,
            outputDataConverter
        );
    }

    @Bean("BuscarReservaPorIdOutputDataConverter")
    public BuscarReservaPorIdOutputDataConverter createOutputDataConverter() {
        return new BuscarReservaPorIdOutputDataConverter();
    }

}
