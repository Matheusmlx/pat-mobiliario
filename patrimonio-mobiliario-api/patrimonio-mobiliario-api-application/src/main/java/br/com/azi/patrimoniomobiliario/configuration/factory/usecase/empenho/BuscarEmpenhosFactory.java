package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.empenho;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.EmpenhoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.buscarlistagem.BuscarEmpenhosUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.buscarlistagem.BuscarEmpenhosUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.buscarlistagem.converter.BuscarEmpenhosFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.buscarlistagem.converter.BuscarEmpenhosOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BuscarEmpenhosFactory {

    @Autowired
    private EmpenhoDataProvider empenhoDataProvider;

    @Bean("BuscarEmpenhosUseCase")
    @DependsOn({"BuscarEmpenhosOutputDataConverter", "BuscarEmpenhosFiltroConverter"})
    public BuscarEmpenhosUseCase createUseCase(BuscarEmpenhosOutputDataConverter outputDataConverter, BuscarEmpenhosFiltroConverter filtroConverter) {
        return new BuscarEmpenhosUseCaseImpl(empenhoDataProvider, outputDataConverter, filtroConverter);
    }

    @Bean("BuscarEmpenhosOutputDataConverter")
    public BuscarEmpenhosOutputDataConverter createOutputDataConverter() {
        return new BuscarEmpenhosOutputDataConverter();
    }

    @Bean("BuscarEmpenhosFiltroConverter")
    public BuscarEmpenhosFiltroConverter createFiltroConverter() {
        return new BuscarEmpenhosFiltroConverter();
    }

}
