package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.configuracao.reserva.global.intervalo;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.listagem.BuscarReservaIntervalosUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.listagem.BuscarReservaIntervalosUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.listagem.converter.BuscarReservaIntervalosFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.listagem.converter.BuscarReservaIntervalosOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BuscarReservaIntervalosFactory {

    @Autowired
    private ReservaDataProvider reservaDataProvider;

    @Autowired
    private ReservaIntervaloDataProvider reservaIntervaloDataProvider;

    @Bean("BuscarOrgaosAdicionadosReservaPatrimonialUseCase")
    public BuscarReservaIntervalosUseCase createUseCase(BuscarReservaIntervalosFiltroConverter filtroConverter,
                                                        BuscarReservaIntervalosOutputDataConverter outpuDataConverter) {
        return new BuscarReservaIntervalosUseCaseImpl(
            reservaDataProvider,
            reservaIntervaloDataProvider,
            filtroConverter,
            outpuDataConverter
        );
    }

    @Bean("BuscarOrgaosAdicionadosReservaPatrimonialFiltroConverter")
    public BuscarReservaIntervalosFiltroConverter createFiltroConverter() {
        return new BuscarReservaIntervalosFiltroConverter();
    }

    @Bean("BuscarOrgaosAdicionadosReservaPatrimonialOutpuDataConverter")
    public BuscarReservaIntervalosOutputDataConverter createOutputDataConverter() {
        return new BuscarReservaIntervalosOutputDataConverter();
    }

}
