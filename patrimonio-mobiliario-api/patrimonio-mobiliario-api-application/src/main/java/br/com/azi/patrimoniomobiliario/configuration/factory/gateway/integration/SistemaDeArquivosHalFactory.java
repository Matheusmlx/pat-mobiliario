package br.com.azi.patrimoniomobiliario.configuration.factory.gateway.integration;

import br.com.azi.patrimoniomobiliario.configuration.PatrimonioMobiliarioProperties;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.SistemaDeArquivosIntegrationHalImpl;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.entity.HalIntegrationProperties;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.downloadfile.DownloadFileIntegrationUseCase;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.metadatafile.MetadataFileIntegrationUseCase;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.promotefile.PromoteFileIntegrationUseCase;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.removedefinitivefile.RemoveDefinitiveFileIntegrationUseCase;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.uploadfile.UploadFileIntegrationUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@RefreshScope
@Configuration
@ConditionalOnProperty(prefix = "az.patrimonio-mobiliario.integration", name = "sistema-de-arquivos", havingValue = "hal", matchIfMissing = true)
public class SistemaDeArquivosHalFactory {

    @Autowired
    private PatrimonioMobiliarioProperties patrimonioMobiliarioProperties;

    @Autowired
    private SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    @Autowired
    private UploadFileIntegrationUseCase uploadFileIntegrationUseCase;

    @Autowired
    private DownloadFileIntegrationUseCase downloadFileIntegrationUseCase;

    @Autowired
    private PromoteFileIntegrationUseCase promoteFileIntegrationUseCase;

    @Autowired
    private RemoveDefinitiveFileIntegrationUseCase removeDefinitiveFileIntegrationUseCase;

    @Autowired
    private MetadataFileIntegrationUseCase metadataFileIntegrationUseCase;

    @Bean("SistemaDeArquivosIntegration")
    @Primary
    public SistemaDeArquivosIntegration createSistemaDeArquivosIntegrationHalImpl() {
        HalIntegrationProperties halProperties = HalIntegrationProperties
                .builder()
                .hal(HalIntegrationProperties.Hal
                    .builder()
                    .repository(patrimonioMobiliarioProperties.getIntegracao().getHal().getRepository())
                    .url(patrimonioMobiliarioProperties.getIntegracao().getHal().getUrl())
                    .build())
                .build();
        return new SistemaDeArquivosIntegrationHalImpl(
            halProperties,
            sessaoUsuarioDataProvider,
            uploadFileIntegrationUseCase,
            promoteFileIntegrationUseCase,
            metadataFileIntegrationUseCase,
            downloadFileIntegrationUseCase,
            removeDefinitiveFileIntegrationUseCase);
    }

}
