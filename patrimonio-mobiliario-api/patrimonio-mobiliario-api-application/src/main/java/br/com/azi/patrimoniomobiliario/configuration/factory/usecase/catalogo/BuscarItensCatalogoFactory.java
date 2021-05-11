package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.catalogo;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ItemCatalogoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.catalogo.buscarlistagem.BuscarItensCatalogoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.catalogo.buscarlistagem.BuscarItensCatalogoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.catalogo.buscarlistagem.converter.BuscarItensCatalogoFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.catalogo.buscarlistagem.converter.BuscarItensCatalogoOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BuscarItensCatalogoFactory {

    @Autowired
    private ItemCatalogoDataProvider itemCatalogoDataProvider;

    @Bean("BuscarItensCatalogoUseCase")
    @DependsOn({"BuscarItensCatalogoFiltroConverter","BuscarItensCatalogoOutputDataConverter"})
    public BuscarItensCatalogoUseCase createUseCase(BuscarItensCatalogoFiltroConverter filtroConverter, BuscarItensCatalogoOutputDataConverter outputDataConverter){
        return new BuscarItensCatalogoUseCaseImpl(
            itemCatalogoDataProvider,
            filtroConverter,
            outputDataConverter
        );
    }
    @Bean("BuscarItensCatalogoOutputDataConverter")
    public BuscarItensCatalogoOutputDataConverter createConverter(){
        return new BuscarItensCatalogoOutputDataConverter();
    }

    @Bean("BuscarItensCatalogoFiltroConverter")
    public BuscarItensCatalogoFiltroConverter createFiltroConverter(){
        return new BuscarItensCatalogoFiltroConverter();
    }
}
