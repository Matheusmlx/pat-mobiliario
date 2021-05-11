package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.documentos;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.buscar.BuscarDocumentosUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.buscar.BuscarDocumentosUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.buscar.converter.BuscarDocumentosOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BuscarDocumentoFactory {

    @Autowired
    private DocumentoDataProvider documentoDataProvider;

    @Bean("BuscarDocumentoUseCase")
    @DependsOn({"BuscarDocumentoOutputDataConverter"})
    public BuscarDocumentosUseCase createUseCase(BuscarDocumentosOutputDataConverter outputDataConverter) {
        return new BuscarDocumentosUseCaseImpl(documentoDataProvider, outputDataConverter);
    }

    @Bean("BuscarDocumentoOutputDataConverter")
    public BuscarDocumentosOutputDataConverter createOutputDataConverter() {
        return new BuscarDocumentosOutputDataConverter();
    }
}
