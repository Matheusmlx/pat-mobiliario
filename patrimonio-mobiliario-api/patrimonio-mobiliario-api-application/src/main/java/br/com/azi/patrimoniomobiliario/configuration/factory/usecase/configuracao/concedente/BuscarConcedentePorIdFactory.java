package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.configuracao.concedente;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ConcedenteDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.buscarporid.BuscarConcedentePorIdUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.buscarporid.BuscarConcedentePorIdUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.buscarporid.converter.BuscarConcedentePorIdOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BuscarConcedentePorIdFactory {

    @Autowired
    private ConcedenteDataProvider concedenteDataProvider;

    @Bean("BuscarConcedentePorIdUseCase")
    @DependsOn({"BuscarConcedentePorIdOutputDataConverter"})
    public BuscarConcedentePorIdUseCase createUsecase(BuscarConcedentePorIdOutputDataConverter converter) {
        return new BuscarConcedentePorIdUseCaseImpl(concedenteDataProvider, converter);
    }

    @Bean("BuscarConcedentePorIdOutputDataConverter")
    public BuscarConcedentePorIdOutputDataConverter createOutputDataConverter() {
        return new BuscarConcedentePorIdOutputDataConverter();
    }
}
