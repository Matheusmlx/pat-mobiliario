package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.documentos;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.exclusao.DeletarDocumentoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.exclusao.DeletarDocumentoUseCaseImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DeletarDocumentoFactory {

    private final DocumentoDataProvider documentoDataProvider;

    private final IncorporacaoDataProvider incorporacaoDataProvider;

    private final SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    @Bean("DeletarDocumentoUseCase")
    public DeletarDocumentoUseCase deletarDocumentoUseCase() {
        return new DeletarDocumentoUseCaseImpl(
            documentoDataProvider,
            incorporacaoDataProvider,
            sistemaDeArquivosIntegration
        );
    }
}
