package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.configuracao.convenio;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ConvenioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.buscarlistagem.BuscarConveniosUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.buscarlistagem.BuscarConveniosUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.buscarlistagem.converter.BuscarConveniosFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.buscarlistagem.converter.BuscarConveniosOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;

@Configuration
public class BuscarConveniosFactory {

    @Autowired
    private ConvenioDataProvider convenioDataProvider;

    @Bean("BuscarConveniosUseCase")
    @DependsOn({"BuscarConveniosFiltroConverter", "BuscarConveniosOutputDataConverter"})
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public BuscarConveniosUseCase createUseCase(BuscarConveniosFiltroConverter filtroConverter, BuscarConveniosOutputDataConverter outputDataConverter) {
        return new BuscarConveniosUseCaseImpl(
            convenioDataProvider,
            outputDataConverter,
            filtroConverter
        );
    }

    @Bean("BuscarConveniosFiltroConverter")
    public BuscarConveniosFiltroConverter createFiltroConverter() {
        return new BuscarConveniosFiltroConverter();
    }

    @Bean("BuscarConveniosOutputDataConverter")
    public BuscarConveniosOutputDataConverter createOutputDataConverter() {
        return new BuscarConveniosOutputDataConverter();
    }
}
