package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.fornecedor;

import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeFornecedoresIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.fornecedor.buscarlistagem.BuscarFornecedoresUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.fornecedor.buscarlistagem.BuscarFornecedoresUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.fornecedor.buscarlistagem.converter.BuscarFornecedoresFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.fornecedor.buscarlistagem.converter.BuscarFornecedoresOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;

@Configuration
public class BuscarFornecedoresFactory {

    @Autowired
    private SistemaDeFornecedoresIntegration sistemaDeFornecedoresIntegration;

    @Bean("BuscarFornecedoresUseCase")
    @DependsOn({"BuscarFornecedoresFiltroConverter", "BuscarFornecedoresOutputDataConverter"})
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public BuscarFornecedoresUseCase createUseCase(BuscarFornecedoresFiltroConverter filtroConverter, BuscarFornecedoresOutputDataConverter outputDataConverter) {
        return new BuscarFornecedoresUseCaseImpl(sistemaDeFornecedoresIntegration, filtroConverter, outputDataConverter);
    }

    @Bean("BuscarFornecedoresFiltroConverter")
    public BuscarFornecedoresFiltroConverter createFiltroConverter() {
        return new BuscarFornecedoresFiltroConverter();
    }

    @Bean("BuscarFornecedoresOutputDataConverter")
    public BuscarFornecedoresOutputDataConverter createOutputDataConverter() {
        return new BuscarFornecedoresOutputDataConverter();
    }
}
