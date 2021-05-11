package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.incorporacao;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.visualizar.VisualizarIncorporacaoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.visualizar.VisualizarIncorporacaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.visualizar.converter.VisualizarIncorporacaoOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class VisualizarIncorporacaoFactory {

    @Autowired
    private IncorporacaoDataProvider incorporacaoDataProvider;

    @Autowired
    private PatrimonioDataProvider patrimonioDataProvider;

    @Bean("VisualizarIncorporacaoUseCase")
    @DependsOn({"VisualizarIncorporacaoOutputDataConverter"})
    public VisualizarIncorporacaoUseCase createUseCase(VisualizarIncorporacaoOutputDataConverter outputDataConverter) {
        return new VisualizarIncorporacaoUseCaseImpl(incorporacaoDataProvider, patrimonioDataProvider, outputDataConverter);
    }

    @Bean("VisualizarIncorporacaoOutputDataConverter")
    public VisualizarIncorporacaoOutputDataConverter createOutputDataConverter() {
        return new VisualizarIncorporacaoOutputDataConverter();
    }

}
