package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.incorporacao;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.buscarsituacao.BuscarSituacaoIncorporacaoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.buscarsituacao.BuscarSituacaoIncorporacaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.buscarsituacao.converter.BuscarSituacaoIncorporacaoOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BuscarSituacaoIncorporacaoFactory {

    @Autowired
    private IncorporacaoDataProvider incorporacaoDataProvider;

    @Bean("BuscarSituacaoIncorporacaoUseCase")
    @DependsOn({"BuscarSituacaoIncorporacaoOutputDataConverter"})
    public BuscarSituacaoIncorporacaoUseCase createUseCase(BuscarSituacaoIncorporacaoOutputDataConverter outputDataConverter) {
        return new BuscarSituacaoIncorporacaoUseCaseImpl(
            incorporacaoDataProvider,
            outputDataConverter
        );
    }

    @Bean("BuscarSituacaoIncorporacaoOutputDataConverter")
    public BuscarSituacaoIncorporacaoOutputDataConverter createOutputDataConverter() {
        return new BuscarSituacaoIncorporacaoOutputDataConverter();
    }
}
