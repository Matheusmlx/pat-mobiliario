package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.patrimonio;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemincorporados.BuscarPatrimoniosIncorporadosUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemincorporados.BuscarPatrimoniosIncorporadosUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemincorporados.converter.BuscarPatrimoniosIncorporadosConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemincorporados.converter.BuscarPatrimoniosIncorporadosFiltroConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BuscarPatrimoniosIncorporadosUseCaseFactory {

    @Autowired
    private PatrimonioDataProvider patrimonioDataProvider;

    @Autowired
    private SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration;

    @Bean("BuscarPatrimoniosIncorporadosUseCase")
    @DependsOn({
        "BuscarPatrimoniosIncorporadosFiltroConverter",
        "BuscarPatrimoniosIncorporadosConverter"
    })
    public BuscarPatrimoniosIncorporadosUseCase createUseCase(BuscarPatrimoniosIncorporadosFiltroConverter filtroConverter, BuscarPatrimoniosIncorporadosConverter outputDataConverter) {
        return new BuscarPatrimoniosIncorporadosUseCaseImpl(
            filtroConverter,
            outputDataConverter,
            sistemaDeGestaoAdministrativaIntegration,
            patrimonioDataProvider
        );
    }

    @Bean("BuscarPatrimoniosIncorporadosFiltroConverter")
    public BuscarPatrimoniosIncorporadosFiltroConverter createFiltroConverter() {
        return new BuscarPatrimoniosIncorporadosFiltroConverter();
    }

    @Bean("BuscarPatrimoniosIncorporadosConverter")
    public BuscarPatrimoniosIncorporadosConverter createOutpuDataConverter() {
        return new BuscarPatrimoniosIncorporadosConverter();
    }
}
