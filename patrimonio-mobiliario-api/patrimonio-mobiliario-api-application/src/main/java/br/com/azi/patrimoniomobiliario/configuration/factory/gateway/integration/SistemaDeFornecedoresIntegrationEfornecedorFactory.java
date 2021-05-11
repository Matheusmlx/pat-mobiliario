package br.com.azi.patrimoniomobiliario.configuration.factory.gateway.integration;

import br.com.azi.patrimoniomobiliario.configuration.PatrimonioMobiliarioProperties;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeFornecedoresIntegration;
import br.com.azi.patrimoniomobiliario.gateway.integration.efornecedor.SistemaDeFornecedoresIntegrationHalImpl;
import br.com.azi.patrimoniomobiliario.gateway.integration.efornecedor.entity.EfornecedorIntegrationProperties;
import br.com.azi.patrimoniomobiliario.gateway.integration.efornecedor.usecase.buscarporfiltro.BuscarFornecedoresPorFiltroIntegrationUseCase;
import br.com.azi.patrimoniomobiliario.gateway.integration.efornecedor.usecase.buscarporid.BuscarFornecedorPorIdIntegrationUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@RefreshScope
@Configuration
@ConditionalOnProperty(prefix = "az.patrimonio-mobiliario.integration", name = "sistema-de-fornecedores", havingValue = "efornecedor", matchIfMissing = true)
public class SistemaDeFornecedoresIntegrationEfornecedorFactory {

    @Autowired
    private PatrimonioMobiliarioProperties patrimonioMobiliarioProperties;

    @Autowired
    private SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    @Autowired
    private BuscarFornecedoresPorFiltroIntegrationUseCase buscarFornecedoresPorFiltroIntegrationUseCase;

    @Autowired
    private BuscarFornecedorPorIdIntegrationUseCase buscarFornecedorPorIdIntegrationUseCase;

    @Bean("SistemaDeFornecedoresIntegration")
    @Primary
    public SistemaDeFornecedoresIntegration createBean() {
        EfornecedorIntegrationProperties efornecedorProperties = EfornecedorIntegrationProperties
            .builder()
            .url(patrimonioMobiliarioProperties.getIntegracao().getEfornecedor().getUrl())
            .build();

        return new SistemaDeFornecedoresIntegrationHalImpl(
            efornecedorProperties,
            sessaoUsuarioDataProvider,
            buscarFornecedoresPorFiltroIntegrationUseCase,
            buscarFornecedorPorIdIntegrationUseCase
        );
    }
}
