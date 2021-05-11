package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.movimentacao.interna.documento;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.buscar.BuscarDocumentosMovimentacaoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.buscar.BuscarDocumentosMovimentacaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.buscar.converter.BuscarDocumentosMovimentacaoOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BuscarDocumentosMovimentacaoFactory {

    @Autowired
    private DocumentoDataProvider documentoDataProvider;

    @Bean("BuscarDocumentosMovimentacaoUseCase")
    @DependsOn({"BuscarDocumentosMovimentacaoOutputDataConverter"})
    public BuscarDocumentosMovimentacaoUseCase createUseCase(BuscarDocumentosMovimentacaoOutputDataConverter converter) {
        return new BuscarDocumentosMovimentacaoUseCaseImpl(
            documentoDataProvider,
            converter
        );
    }

    @Bean("BuscarDocumentosMovimentacaoOutputDataConverter")
    public BuscarDocumentosMovimentacaoOutputDataConverter createConverter() {
        return new BuscarDocumentosMovimentacaoOutputDataConverter();
    }
}
