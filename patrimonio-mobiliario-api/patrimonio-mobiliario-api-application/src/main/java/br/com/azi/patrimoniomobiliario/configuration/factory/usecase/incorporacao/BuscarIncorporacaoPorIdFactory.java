package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.incorporacao;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.buscarporid.BuscarIncorporacaoPorIdUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.buscarporid.BuscarIncorporacaoPorIdUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.buscarporid.converter.BuscarIncorporacaoPorIdOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BuscarIncorporacaoPorIdFactory {

    @Autowired
    private IncorporacaoDataProvider incorporacaoDataProvider;

    @Bean("BuscarIncorporacaoPorIdUseCase")
    @DependsOn({"BuscarIncorporacaoPorIdOutputDataConverter"})
    public BuscarIncorporacaoPorIdUseCase createUseCase(BuscarIncorporacaoPorIdOutputDataConverter outputDataConverter) {
        return new BuscarIncorporacaoPorIdUseCaseImpl(incorporacaoDataProvider, outputDataConverter);
    }

    @Bean("BuscarIncorporacaoPorIdOutputDataConverter")
    public BuscarIncorporacaoPorIdOutputDataConverter createOutputDataConverter() {
        return new BuscarIncorporacaoPorIdOutputDataConverter();
    }

}
