package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.incorporacao.item;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ConfigContaContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ItemIncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.listagem.BuscarItensIncorporacaoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.listagem.BuscarItensIncorporacaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.listagem.converter.BuscarItensIncorporacaoFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.listagem.converter.BuscarItensIncorporacaoOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BuscarItensIncorporacaoFactory {

    @Autowired
    private ItemIncorporacaoDataProvider incorporacaoDataProvider;

    @Autowired
    private ConfigContaContabilDataProvider configContaContabilDataProvider;

    @Autowired
    private SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    @Bean("BuscarItensIncorporacaoUseCase")
    @DependsOn({"BuscarItensIncorporacaoFiltroConverter","BuscarItensIncorporacaoOutputDataConverter"})
    public BuscarItensIncorporacaoUseCase createUseCase(BuscarItensIncorporacaoFiltroConverter filtroConverter, BuscarItensIncorporacaoOutputDataConverter outputDataConverter){
        return new BuscarItensIncorporacaoUseCaseImpl(
            incorporacaoDataProvider,
            outputDataConverter,
            filtroConverter,
            sistemaDeArquivosIntegration
        );
    }

    @Bean("BuscarItensIncorporacaoFiltroConverter")
    public BuscarItensIncorporacaoFiltroConverter createFiltroConverter(){
        return new BuscarItensIncorporacaoFiltroConverter();
    }

    @Bean("BuscarItensIncorporacaoOutputDataConverter")
    public BuscarItensIncorporacaoOutputDataConverter createConverter(){
        return new BuscarItensIncorporacaoOutputDataConverter();
    }
}
