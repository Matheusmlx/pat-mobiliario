package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.patrimonio;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ConfigContaContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ConfiguracaoDepreciacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ItemIncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.buscarporid.BuscarPatrimonioPorIdUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.buscarporid.BuscarPatrimonioPorIdUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.buscarporid.converter.BuscarPatrimonioPorIdOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BuscarPatrimoniosPorIdFactory {

    @Autowired
    private PatrimonioDataProvider patrimonioDataProvider;

    @Autowired
    private IncorporacaoDataProvider incorporacaoDataProvider;

    @Autowired
    private ItemIncorporacaoDataProvider itemIncorporacaoDataProvider;

    @Autowired
    private ConfigContaContabilDataProvider configContaContabilDataProvider;

    @Autowired
    private SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    @Autowired
    private ConfiguracaoDepreciacaoDataProvider configuracaoDepreciacaoDataProvider;


    @Bean("BuscarPatrimonioPorIdUseCase")
    @DependsOn({"BuscarPatrimonioPorIdOutputDataConverter"})
    public BuscarPatrimonioPorIdUseCase createUseCase(BuscarPatrimonioPorIdOutputDataConverter outputDataConverter) {
        return new BuscarPatrimonioPorIdUseCaseImpl(
            patrimonioDataProvider,
            incorporacaoDataProvider,
            itemIncorporacaoDataProvider,
            configContaContabilDataProvider,
            configuracaoDepreciacaoDataProvider,
            sistemaDeArquivosIntegration,
            outputDataConverter
        );
    }


    @Bean("BuscarPatrimonioPorIdOutputDataConverter")
    public BuscarPatrimonioPorIdOutputDataConverter createOutputDataConverter() {
        return new BuscarPatrimonioPorIdOutputDataConverter();
    }
}
