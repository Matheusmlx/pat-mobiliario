package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.movimentacao.interna.documento;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.remover.RemoverDocumentoMovimentacaoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.remover.RemoverDocumentoMovimentacaoUseCaseImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RemoverDocumentoMovimentacaoFactory {

    @Autowired
    private DocumentoDataProvider documentoDataProvider;

    @Autowired
    private SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    @Bean("RemoverDocumentoMovimentacaoUseCase")
    public RemoverDocumentoMovimentacaoUseCase removerDocumentoMovimentacaoUseCase() {
        return new RemoverDocumentoMovimentacaoUseCaseImpl(documentoDataProvider, sistemaDeArquivosIntegration);
    }
}
