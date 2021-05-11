package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.configuracao.reserva;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.listagem.BuscarReservasUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.listagem.BuscarReservasUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.listagem.converter.BuscarReservasFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.listagem.converter.BuscarReservasOutputDataConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BuscarReservasFactory {

    private final ReservaDataProvider reservaDataProvider;

    private final ReservaIntervaloDataProvider reservaIntervaloDataProvider;

    private final SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    @Bean("BuscarReservasUseCase")
    public BuscarReservasUseCase createUseCase(BuscarReservasFiltroConverter filtroConverter, BuscarReservasOutputDataConverter outputDataConverter) {
        return new BuscarReservasUseCaseImpl(
            reservaDataProvider,
            reservaIntervaloDataProvider,
            sessaoUsuarioDataProvider,
            filtroConverter,
            outputDataConverter
        );
    }

    @Bean("BuscarReservasFiltroConverter")
    public BuscarReservasFiltroConverter createFiltroConverter() {
        return new BuscarReservasFiltroConverter();
    }

    @Bean("BuscarReservasOutputDataConverter")
    public BuscarReservasOutputDataConverter createOutputDataConverter() {
        return new BuscarReservasOutputDataConverter();
    }
}
