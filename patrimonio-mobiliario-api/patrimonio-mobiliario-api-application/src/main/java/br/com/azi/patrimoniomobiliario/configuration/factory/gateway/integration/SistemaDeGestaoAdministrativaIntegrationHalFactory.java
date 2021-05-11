package br.com.azi.patrimoniomobiliario.configuration.factory.gateway.integration;

import br.com.azi.patrimoniomobiliario.configuration.PatrimonioMobiliarioProperties;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.SistemaDeGestaoAdministrativaIntegrationHalImpl;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.entity.HalIntegrationProperties;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.buscarfundosporfuncao.BuscarFundosPorFuncaoIntegrationUseCase;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.buscarorgaosporfiltro.BuscarOrgaosPorFiltroEFuncaoUseCase;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.buscarsetorespororgaos.BuscarSetoresPorOrgaosIntegrationUseCase;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.buscarsetoresquenaosaoalmoxarifadopororgaoefuncao.BuscarSetoresQueNaoSaoAlmoxarifadoPorOrgaoEFuncaoUseCase;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.buscarunidadeorganozacionalporid.BuscarUnidadeOrganizacionalPorIdIntegrationUseCase;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.buscarunidadesorganizacionais.BuscarUnidadesOrganizacionaisIntegrationUseCase;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.buscarunidadesorganizacionaisporfuncao.BuscarUnidadesOrganizacionaisPorFuncaoIntegrationUseCase;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.verificardominiounidadeorganizacionalporidefuncao.VerificarDominioUnidadeOrganizacionalPorIdEFuncaoIntegrationUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@RefreshScope
@Configuration
@ConditionalOnProperty(prefix = "az.patrimonio-mobiliario.integration", name = "sistema-de-gestao-administrativa", havingValue = "hal", matchIfMissing = true)
public class SistemaDeGestaoAdministrativaIntegrationHalFactory {

    @Autowired
    private PatrimonioMobiliarioProperties patrimonioMobiliarioProperties;

    @Autowired
    private SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    @Autowired
    private BuscarUnidadesOrganizacionaisIntegrationUseCase buscarUnidadesOrganizacionaisIntegrationUseCase;

    @Autowired
    private BuscarUnidadesOrganizacionaisPorFuncaoIntegrationUseCase buscarUnidadesOrganizacionaisPorFuncaoIntegrationUseCase;

    @Autowired
    private BuscarUnidadeOrganizacionalPorIdIntegrationUseCase buscarUnidadeOrganizacionalPorIdIntegrationUseCase;

    @Autowired
    private VerificarDominioUnidadeOrganizacionalPorIdEFuncaoIntegrationUseCase verificarDominioUnidadeOrganizacionalPorIdEFuncaoIntegrationUseCase;

    @Autowired
    private BuscarSetoresPorOrgaosIntegrationUseCase buscarSetoresPorOrgaosIntegrationUseCase;

    @Autowired
    private BuscarFundosPorFuncaoIntegrationUseCase buscarFundosPorFuncaoIntegrationUseCase;

    @Autowired
    private BuscarSetoresQueNaoSaoAlmoxarifadoPorOrgaoEFuncaoUseCase buscarSetoresQueNaoSaoAlmoxarifadoPorOrgaoEFuncaoUseCase;

    @Autowired
    private BuscarOrgaosPorFiltroEFuncaoUseCase buscarOrgaosPorFiltroEFuncaoUseCase;

    @Bean("SistemaDeGestaoAdministrativaIntegration")
    @Primary
    public SistemaDeGestaoAdministrativaIntegration createBean() {
        HalIntegrationProperties halProperties = HalIntegrationProperties
            .builder()
            .hal(
                HalIntegrationProperties.Hal
                    .builder()
                    .repository(patrimonioMobiliarioProperties.getIntegracao().getHal().getRepository())
                    .url(patrimonioMobiliarioProperties.getIntegracao().getHal().getUrl())
                    .build()
            )
            .halConfig(
                HalIntegrationProperties.HalConfig
                    .builder()
                    .url(patrimonioMobiliarioProperties.getIntegracao().getHalConfig().getUrl())
                    .build()
            )
            .build();

        return new SistemaDeGestaoAdministrativaIntegrationHalImpl(
            halProperties,
            sessaoUsuarioDataProvider,
            buscarUnidadesOrganizacionaisIntegrationUseCase,
            buscarUnidadesOrganizacionaisPorFuncaoIntegrationUseCase,
            buscarUnidadeOrganizacionalPorIdIntegrationUseCase,
            verificarDominioUnidadeOrganizacionalPorIdEFuncaoIntegrationUseCase,
            buscarSetoresPorOrgaosIntegrationUseCase,
            buscarFundosPorFuncaoIntegrationUseCase,
            buscarSetoresQueNaoSaoAlmoxarifadoPorOrgaoEFuncaoUseCase,
            buscarOrgaosPorFiltroEFuncaoUseCase
        );
    }
}
