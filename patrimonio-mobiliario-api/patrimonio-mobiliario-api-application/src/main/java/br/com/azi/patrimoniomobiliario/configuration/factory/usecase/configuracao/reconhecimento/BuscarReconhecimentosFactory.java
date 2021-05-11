package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.configuracao.reconhecimento;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReconhecimentoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.buscarlistagem.BuscarReconhecimentosUsecase;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.buscarlistagem.BuscarReconhecimentosUsecaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.buscarlistagem.converter.BuscarReconhecimentosFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.buscarlistagem.converter.BuscarReconhecimentosOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;

@Configuration
public class BuscarReconhecimentosFactory {

    @Autowired
    private ReconhecimentoDataProvider reconhecimentoDataProvider;

    @Bean("BuscarReconhecimentosUsecase")
    @DependsOn({"BuscarReconhecimentosFiltroConverter", "BuscarReconhecimentosOutputDataConverter"})
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public BuscarReconhecimentosUsecase createUseCase(BuscarReconhecimentosFiltroConverter filtroConverter, BuscarReconhecimentosOutputDataConverter outputDataConverter) {
        return new BuscarReconhecimentosUsecaseImpl(
            reconhecimentoDataProvider,
            outputDataConverter,
            filtroConverter
        );
    }

    @Bean("BuscarReconhecimentosFiltroConverter")
    public BuscarReconhecimentosFiltroConverter createFiltroConverter() {
        return new BuscarReconhecimentosFiltroConverter();
    }

    @Bean("BuscarReconhecimentosOutputDataConverter")
    public BuscarReconhecimentosOutputDataConverter createOutputDataConverter() {
        return new BuscarReconhecimentosOutputDataConverter();
    }
}
