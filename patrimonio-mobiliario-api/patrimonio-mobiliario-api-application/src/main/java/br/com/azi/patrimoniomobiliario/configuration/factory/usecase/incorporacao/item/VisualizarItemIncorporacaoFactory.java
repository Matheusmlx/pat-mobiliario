package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.incorporacao.item;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ItemIncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.visualizar.VisualizarItemIncorporacaoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.visualizar.VisualizarItemIncorporacaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.visualizar.converter.VisualizarItemIncorporacaoOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class VisualizarItemIncorporacaoFactory {

    @Autowired
    private ItemIncorporacaoDataProvider itemIncorporacaoDataProvider;

    @Autowired
    private SistemaDeArquivosIntegration sistemaDeArquivosIntegration;


    @Bean("VisualizarItemIncorporacaoUseCase")
    @DependsOn({"VisualizarItemIncorporacaoOutputDataConverter"})
    public VisualizarItemIncorporacaoUseCase useCase(VisualizarItemIncorporacaoOutputDataConverter outputDataConverter){
        return new VisualizarItemIncorporacaoUseCaseImpl(
            itemIncorporacaoDataProvider,
            outputDataConverter,
            sistemaDeArquivosIntegration
        );
    }

    @Bean("VisualizarItemIncorporacaoOutputDataConverter")
    public VisualizarItemIncorporacaoOutputDataConverter createConverter(){
        return new VisualizarItemIncorporacaoOutputDataConverter();
    }
}
