package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.patrimonio;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagem.BuscarPatrimoniosPorItemIncorporacaoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagem.BuscarPatrimoniosPorItemIncorporacaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagem.converter.BuscarPatrimoniosPorItemIncorporacaoFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagem.converter.BuscarPatrimoniosPorItemIncorporacaoOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BuscarPatrimoniosPorItemIncorporacaoFactory {

    @Autowired
    private PatrimonioDataProvider patrimonioDataProvider;

    @Autowired
    private SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    @Bean("BuscarPatrimoniosPorItemIncorporacaoUseCase")
    @DependsOn({
        "BuscarPatrimoniosPorItemIncorporacaoFiltroConverter",
        "BuscarPatrimoniosPorItemIncorporacaoOutputDataConverter"
    })
    public BuscarPatrimoniosPorItemIncorporacaoUseCase createUseCase(BuscarPatrimoniosPorItemIncorporacaoFiltroConverter filtroConverter, BuscarPatrimoniosPorItemIncorporacaoOutputDataConverter outputDataConverter) {
        return new BuscarPatrimoniosPorItemIncorporacaoUseCaseImpl(patrimonioDataProvider, filtroConverter, outputDataConverter, sistemaDeArquivosIntegration);
    }

    @Bean("BuscarPatrimoniosPorItemIncorporacaoFiltroConverter")
    public BuscarPatrimoniosPorItemIncorporacaoFiltroConverter createFiltroConverter() {
        return new BuscarPatrimoniosPorItemIncorporacaoFiltroConverter();
    }

    @Bean("BuscarPatrimoniosPorItemIncorporacaoOutputDataConverter")
    public BuscarPatrimoniosPorItemIncorporacaoOutputDataConverter createOutputDataConverter() {
        return new BuscarPatrimoniosPorItemIncorporacaoOutputDataConverter();
    }
}
