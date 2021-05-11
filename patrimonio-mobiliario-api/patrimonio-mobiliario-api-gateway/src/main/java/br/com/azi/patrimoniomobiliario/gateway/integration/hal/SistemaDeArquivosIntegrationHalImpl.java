package br.com.azi.patrimoniomobiliario.gateway.integration.hal;

import br.com.azi.patrimoniomobiliario.domain.entity.Arquivo;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.entity.HalIntegrationProperties;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.downloadfile.DownloadFileIntegrationUseCase;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.metadatafile.MetadataFileIntegrationUseCase;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.promotefile.PromoteFileIntegrationUseCase;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.removedefinitivefile.RemoveDefinitiveFileIntegrationUseCase;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.uploadfile.UploadFileIntegrationUseCase;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public class SistemaDeArquivosIntegrationHalImpl implements SistemaDeArquivosIntegration {

    private HalIntegrationProperties integrationProperties;

    private SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    private UploadFileIntegrationUseCase uploadFileIntegrationUseCase;

    private PromoteFileIntegrationUseCase promoteFileIntegrationUseCase;

    private MetadataFileIntegrationUseCase metadataFileIntegrationUseCase;

    private DownloadFileIntegrationUseCase downloadFileIntegrationUseCase;

    private RemoveDefinitiveFileIntegrationUseCase removeDefinitiveFileIntegrationUseCase;

    @Override
    public Arquivo download(String fileUri) {
        return downloadFileIntegrationUseCase.executar(integrationProperties, sessaoUsuarioDataProvider.get(), fileUri);
    }

    @Override
    public Arquivo upload(Arquivo arquivo) {
        return uploadFileIntegrationUseCase.executar(integrationProperties, sessaoUsuarioDataProvider.get(), arquivo);
    }

    @Override
    public void promote(Arquivo arquivo) {
        promoteFileIntegrationUseCase.executar(integrationProperties, sessaoUsuarioDataProvider.get(), arquivo);
    }

    @Override
    public void removeDefinitiveFile(Arquivo arquivo) {
        removeDefinitiveFileIntegrationUseCase.executar(integrationProperties, sessaoUsuarioDataProvider.get(), arquivo);
    }

    @Override
    public Arquivo getMetadata(String fileUri) {
        return metadataFileIntegrationUseCase.executar(integrationProperties, sessaoUsuarioDataProvider.get(), fileUri);
    }
}
