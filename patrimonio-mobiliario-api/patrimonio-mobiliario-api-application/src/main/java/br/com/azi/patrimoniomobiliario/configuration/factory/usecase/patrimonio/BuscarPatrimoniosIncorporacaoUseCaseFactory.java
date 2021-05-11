package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.patrimonio;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ItemIncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemporincorporacao.BuscarPatrimoniosIncorporacaoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemporincorporacao.BuscarPatrimoniosIncorporacaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemporincorporacao.converter.BuscarPatrimoniosIncorporacaoFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemporincorporacao.converter.BuscarPatrimoniosIncorporacaoOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BuscarPatrimoniosIncorporacaoUseCaseFactory {

    @Autowired
    private ItemIncorporacaoDataProvider itemIncorporacaoDataProvider;

    @Autowired
    private PatrimonioDataProvider patrimonioDataProvider;

    @Bean("BuscarPatrimoniosIncorporacaoUseCase")
    public BuscarPatrimoniosIncorporacaoUseCase createUseCase(
        BuscarPatrimoniosIncorporacaoOutputDataConverter outputDataConverter,
        BuscarPatrimoniosIncorporacaoFiltroConverter filtroConverter) {
        return new BuscarPatrimoniosIncorporacaoUseCaseImpl(
            itemIncorporacaoDataProvider,
            patrimonioDataProvider,
            outputDataConverter,
            filtroConverter
        );
    }

    @Bean("BuscarPatrimoniosIncorporacaoOutputDataConverter")
    public BuscarPatrimoniosIncorporacaoOutputDataConverter createOutputDataConverter() {
        return new BuscarPatrimoniosIncorporacaoOutputDataConverter();
    }

    @Bean("BuscarPatrimoniosIncorporacaoFiltroConverter")
    public BuscarPatrimoniosIncorporacaoFiltroConverter createFiltroConverter() {
        return new BuscarPatrimoniosIncorporacaoFiltroConverter();
    }
}

