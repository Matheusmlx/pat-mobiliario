package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.configuracao.concedente;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ConcedenteDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.buscarlistagem.BuscarConcedentesUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.buscarlistagem.BuscarConcedentesUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.buscarlistagem.converter.BuscarConcedentesFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.buscarlistagem.converter.BuscarConcedentesOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;

@Configuration
public class BuscarConcedentesFactory {

    @Autowired
    private ConcedenteDataProvider concedenteDataProvider;

    @Bean("BuscarConcedentesUseCase")
    @DependsOn({"BuscarConcedentesFiltroConverter", "BuscarConcedentesOutputDataConverter"})
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public BuscarConcedentesUseCase createUseCase(BuscarConcedentesFiltroConverter filtroConverter, BuscarConcedentesOutputDataConverter outputDataConverter) {
        return new BuscarConcedentesUseCaseImpl(
            concedenteDataProvider,
            outputDataConverter,
            filtroConverter
        );
    }

    @Bean("BuscarConcedentesFiltroConverter")
    public BuscarConcedentesFiltroConverter createFiltroConverter() {
        return new BuscarConcedentesFiltroConverter();
    }

    @Bean("BuscarConcedentesOutputDataConverter")
    public BuscarConcedentesOutputDataConverter createOutputDataConverter() {
        return new BuscarConcedentesOutputDataConverter();
    }
}
