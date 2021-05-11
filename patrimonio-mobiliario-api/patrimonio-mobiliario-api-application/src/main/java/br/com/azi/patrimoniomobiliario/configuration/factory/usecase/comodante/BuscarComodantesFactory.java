package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.comodante;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ComodanteDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.comodante.listagem.BuscarComodantesUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.comodante.listagem.BuscarComodantesUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.comodante.listagem.converter.BuscarComodantesFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.comodante.listagem.converter.BuscarComodantesOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BuscarComodantesFactory {

    @Autowired
    private ComodanteDataProvider comodanteDataProvider;

    @Bean("BuscarComodantesUseCase")
    @DependsOn({"BuscarComodantesOutputDataConverter", "BuscarComodantesFiltroConverter"})
    public BuscarComodantesUseCase createUseCase(BuscarComodantesOutputDataConverter outputDataConverter,
                                                 BuscarComodantesFiltroConverter filtroConverter) {
        return new BuscarComodantesUseCaseImpl(
            comodanteDataProvider,
            outputDataConverter,
            filtroConverter
        );
    }

    @Bean("BuscarComodantesOutputDataConverter")
    public BuscarComodantesOutputDataConverter createOutputDataConverter() {
        return new BuscarComodantesOutputDataConverter();
    }

    @Bean("BuscarComodantesFiltroConverter")
    public BuscarComodantesFiltroConverter createFiltroConverter() {
        return new BuscarComodantesFiltroConverter();
    }

}
