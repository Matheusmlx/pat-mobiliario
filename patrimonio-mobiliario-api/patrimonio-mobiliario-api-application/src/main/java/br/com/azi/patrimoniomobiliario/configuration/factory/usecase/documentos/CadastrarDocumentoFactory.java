package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.documentos;


import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.TipoDocumentoDataprovider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.cadastro.CadastrarDocumentoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.cadastro.CadastrarDocumentoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.cadastro.converter.CadastrarDocumentoOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class CadastrarDocumentoFactory {

    @Autowired
    private TipoDocumentoDataprovider tipoDocumentoDataprovider;

    @Autowired
    private IncorporacaoDataProvider incorporacaoDataProvider;

    @Autowired
    private CadastrarDocumentoOutputDataConverter cadastrarDocumentoOutputDataConverter;

    @Autowired
    private SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    @Autowired
    private DocumentoDataProvider documentoDataProvider;

    @Bean("CadastrarDocumentoUseCase")
    @DependsOn({"CadastrarDocumentoOutputDataConverter"})
    public CadastrarDocumentoUseCase createUseCase(CadastrarDocumentoOutputDataConverter outputDataConverter) {
        return new CadastrarDocumentoUseCaseImpl(
            incorporacaoDataProvider,
            tipoDocumentoDataprovider,
            documentoDataProvider,
            cadastrarDocumentoOutputDataConverter,
            sistemaDeArquivosIntegration
        );
    }

    @Bean("CadastrarDocumentoOutputDataConverter")
    public CadastrarDocumentoOutputDataConverter createConverter() {
        return new CadastrarDocumentoOutputDataConverter();
    }
}

