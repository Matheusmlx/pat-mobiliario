package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.movimentacao.interna.documento;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.TipoDocumentoDataprovider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.cadastro.CadastrarDocumentoMovimentacaoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.cadastro.CadastrarDocumentoMovimentacaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.cadastro.converter.CadastrarDocumentoMovimentacaoOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class CadastrarDocumentoMovimentacaoFactory {

    @Autowired
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Autowired
    private TipoDocumentoDataprovider tipoDocumentoDataprovider;

    @Autowired
    private DocumentoDataProvider documentoDataProvider;

    @Autowired
    private SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    @Bean()
    @DependsOn({"CadastrarDocumentoMovimentacaoOutputDataConverter"})
    public CadastrarDocumentoMovimentacaoUseCase createUseCase(CadastrarDocumentoMovimentacaoOutputDataConverter converter) {
        return new CadastrarDocumentoMovimentacaoUseCaseImpl(
            movimentacaoDataProvider,
            tipoDocumentoDataprovider,
            documentoDataProvider,
            sistemaDeArquivosIntegration,
            converter
        );
    }

    @Bean("CadastrarDocumentoMovimentacaoOutputDataConverter")
    public CadastrarDocumentoMovimentacaoOutputDataConverter createConverter() {
        return new CadastrarDocumentoMovimentacaoOutputDataConverter();
    }
}
