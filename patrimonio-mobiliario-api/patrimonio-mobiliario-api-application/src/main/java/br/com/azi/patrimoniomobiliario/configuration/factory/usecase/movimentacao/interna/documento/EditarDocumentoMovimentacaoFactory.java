package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.movimentacao.interna.documento;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.TipoDocumentoDataprovider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.edicao.EditarDocumentoMovimentacaoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.edicao.EditarDocumentoMovimentacaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.edicao.converter.EditarDocumentoMovimentacaoOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class EditarDocumentoMovimentacaoFactory {

    @Autowired
    private DocumentoDataProvider documentoDataProvider;

    @Autowired
    private TipoDocumentoDataprovider tipoDocumentoDataprovider;

    @Autowired
    private SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    @Bean("EditarDocumentoMovimentacaoUseCase")
    @DependsOn({"EditarDocumentoMovimentacaoOutputDataConverter"})
    public EditarDocumentoMovimentacaoUseCase createUseCase(EditarDocumentoMovimentacaoOutputDataConverter converter) {
        return new EditarDocumentoMovimentacaoUseCaseImpl(
            documentoDataProvider,
            tipoDocumentoDataprovider,
            sistemaDeArquivosIntegration,
            converter
        );
    }

    @Bean("EditarDocumentoMovimentacaoOutputDataConverter")
    public EditarDocumentoMovimentacaoOutputDataConverter createConverter() {
        return new EditarDocumentoMovimentacaoOutputDataConverter();
    }
}
