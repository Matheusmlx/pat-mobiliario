package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.documentos;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.editar.EditarDocumentoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.editar.EditarDocumentoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.editar.converter.EditarDocumentoOutputDataConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
@RequiredArgsConstructor
public class EditarDocumentoFactory {

    private final DocumentoDataProvider documentoDataProvider;

    private final IncorporacaoDataProvider incorporacaoDataProvider;

    private final SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    @Bean("EditarDocumentoUseCase")
    @DependsOn({"EditarDocumentoOutputDataConverter"})
    public EditarDocumentoUseCase createUseCase(EditarDocumentoOutputDataConverter outputDataConverter) {
        return new EditarDocumentoUseCaseImpl(
            documentoDataProvider,
            incorporacaoDataProvider,
            outputDataConverter,
            sistemaDeArquivosIntegration
        );
    }

    @Bean("EditarDocumentoOutputDataConverter")
    public EditarDocumentoOutputDataConverter createConverter() {
        return new EditarDocumentoOutputDataConverter();
    }
}
