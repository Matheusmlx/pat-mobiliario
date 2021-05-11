package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.incorporacao.item;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ItemIncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.buscarporid.BuscarItemIncorporacaoPorIdUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.buscarporid.BuscarItemIncorporacaoPorIdUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.buscarporid.converter.BuscarItemIncorporacaoPorIdOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BuscarItemIncorporacaoPorIdFactory {

    @Autowired
    private ItemIncorporacaoDataProvider itemIncorporacaoDataProvider;

    @Bean("BuscarItemIncorporacaoPorIdUseCase")
    @DependsOn({"BuscarItemIncorporacaoPorIdOutputDataConverter"})
    public BuscarItemIncorporacaoPorIdUseCase useCase(BuscarItemIncorporacaoPorIdOutputDataConverter outputDataConverter){
        return new BuscarItemIncorporacaoPorIdUseCaseImpl(
            itemIncorporacaoDataProvider,
            outputDataConverter
        );
    }

    @Bean("BuscarItemIncorporacaoPorIdOutputDataConverter")
    public BuscarItemIncorporacaoPorIdOutputDataConverter createConverter(){
        return new BuscarItemIncorporacaoPorIdOutputDataConverter();
    }
}
