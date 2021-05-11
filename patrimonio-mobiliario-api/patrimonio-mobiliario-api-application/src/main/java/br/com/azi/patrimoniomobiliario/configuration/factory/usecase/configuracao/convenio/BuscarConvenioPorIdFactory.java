package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.configuracao.convenio;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ConvenioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.buscarporid.BuscarConvenioPorIdUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.buscarporid.BuscarConvenioPorIdUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.buscarporid.converter.BuscarConvenioPorIdOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BuscarConvenioPorIdFactory {

    @Autowired
    private ConvenioDataProvider convenioDataProvider;

    @Bean("BuscarConvenioPorIdUseCase")
    @DependsOn({"BuscarConvenioPorIdOutputDataConverter"})
    public BuscarConvenioPorIdUseCase createUsecase(BuscarConvenioPorIdOutputDataConverter converter) {
        return new BuscarConvenioPorIdUseCaseImpl(convenioDataProvider, converter);
    }

    @Bean("BuscarConvenioPorIdOutputDataConverter")
    public BuscarConvenioPorIdOutputDataConverter createOutputDataConverter() {
        return new BuscarConvenioPorIdOutputDataConverter();
    }
}
