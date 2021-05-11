package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.patrimonio;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ItemIncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemestornados.BuscarPatrimoniosEstornadosPorIncorporacaoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemestornados.BuscarPatrimoniosEstornadosPorIncorporacaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemestornados.converter.BuscarPatrimoniosEstornadosPorIncorporacaoFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemestornados.converter.BuscarPatrimoniosEstornadosPorIncorporacaoOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BuscarPatrimoniosEstornadosPorIncorporacaoFactory {

    @Autowired
    private ItemIncorporacaoDataProvider itemIncorporacaoDataProvider;

    @Autowired
    private PatrimonioDataProvider patrimonioDataProvider;

    @Bean("BuscarPatrimoniosEstornadosPorIncorporacaoUseCase")
    public BuscarPatrimoniosEstornadosPorIncorporacaoUseCase createUseCase(
        BuscarPatrimoniosEstornadosPorIncorporacaoFiltroConverter filtroConverter,
        BuscarPatrimoniosEstornadosPorIncorporacaoOutputDataConverter outputDataConverter) {
        return new BuscarPatrimoniosEstornadosPorIncorporacaoUseCaseImpl(
            itemIncorporacaoDataProvider,
            patrimonioDataProvider,
            filtroConverter,
            outputDataConverter
        );
    }

    @Bean("BuscarPatrimoniosEstornadosPorIncorporacaoFiltroConverter")
    public BuscarPatrimoniosEstornadosPorIncorporacaoFiltroConverter createFiltroConverter() {
        return new BuscarPatrimoniosEstornadosPorIncorporacaoFiltroConverter();
    }

    @Bean("BuscarPatrimoniosEstornadosPorIncorporacaoOutputDataConverter")
    public BuscarPatrimoniosEstornadosPorIncorporacaoOutputDataConverter createOutputDataConverter() {
        return new BuscarPatrimoniosEstornadosPorIncorporacaoOutputDataConverter();
    }

}
