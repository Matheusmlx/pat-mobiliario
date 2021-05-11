package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.fornecedor;

import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeFornecedoresIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.fornecedor.buscarporid.BuscarFornecedorPorIdUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.fornecedor.buscarporid.BuscarFornecedorPorIdUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.fornecedor.buscarporid.converter.BuscarFornecedorPorIdOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;

@Configuration
public class BuscarFornecedorPorIdFactory {

    @Autowired
    private SistemaDeFornecedoresIntegration sistemaDeFornecedoresIntegration;

    @Bean("BuscarFornecedorPorIdUseCase")
    @DependsOn({"BuscarFornecedorPorIdOutputDataConverter"})
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public BuscarFornecedorPorIdUseCase createUseCase(BuscarFornecedorPorIdOutputDataConverter outputDataConverter) {
        return new BuscarFornecedorPorIdUseCaseImpl(sistemaDeFornecedoresIntegration, outputDataConverter);
    }

    @Bean("BuscarFornecedorPorIdOutputDataConverter")
    public BuscarFornecedorPorIdOutputDataConverter createOutputDataConverter() {
        return new BuscarFornecedorPorIdOutputDataConverter();
    }
}
