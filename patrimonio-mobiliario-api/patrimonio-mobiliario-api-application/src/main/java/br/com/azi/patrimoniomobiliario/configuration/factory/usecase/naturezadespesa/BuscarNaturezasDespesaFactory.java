package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.naturezadespesa;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ItemCatalogoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NaturezaDespesaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.naturezadespesa.buscar.BuscarNaturezasDespesaUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.naturezadespesa.buscar.BuscarNaturezasDespesaUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.naturezadespesa.buscar.conveter.BuscarNaturezasDespesaOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BuscarNaturezasDespesaFactory {

    @Autowired
    private NaturezaDespesaDataProvider naturezaDespesaDataProvider;

    @Autowired
    private ItemCatalogoDataProvider itemCatalogoDataProvider;

    @Bean("BuscarNaturezasDespesaUseCase")
    @DependsOn({"BuscarNaturezasDespesaOutputDataConverter"})
    public BuscarNaturezasDespesaUseCase createUseCase(BuscarNaturezasDespesaOutputDataConverter outputDataConverter){
        return new BuscarNaturezasDespesaUseCaseImpl(
            naturezaDespesaDataProvider,
            itemCatalogoDataProvider,
            outputDataConverter
        );
    }


    @Bean("BuscarNaturezasDespesaOutputDataConverter")
    public BuscarNaturezasDespesaOutputDataConverter createConverter(){
        return new BuscarNaturezasDespesaOutputDataConverter();
    }
}
