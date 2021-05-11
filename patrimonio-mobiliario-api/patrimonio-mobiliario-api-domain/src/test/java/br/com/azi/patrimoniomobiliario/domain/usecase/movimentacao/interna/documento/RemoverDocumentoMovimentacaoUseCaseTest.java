package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento;


import br.com.azi.patrimoniomobiliario.domain.entity.Documento;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.remover.RemoverDocumentoMovimentacaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.remover.RemoverDocumentoMovimentacaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.remover.exception.DocumentoNaoEncontradoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.remover.exception.DocumentoNaoPertenceAMovimentacaoException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class RemoverDocumentoMovimentacaoUseCaseTest {

    DocumentoDataProvider documentoDataProvider;

    SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    @Before
    public void inicializa() {
        documentoDataProvider = Mockito.mock(DocumentoDataProvider.class);
        sistemaDeArquivosIntegration = Mockito.mock(SistemaDeArquivosIntegration.class);
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoMovimentacaoForNula() {
        RemoverDocumentoMovimentacaoUseCaseImpl usecase = new RemoverDocumentoMovimentacaoUseCaseImpl(documentoDataProvider, sistemaDeArquivosIntegration);

        usecase.executar(new RemoverDocumentoMovimentacaoInputData(1L, null));
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoIdForNulo() {
        RemoverDocumentoMovimentacaoUseCaseImpl usecase = new RemoverDocumentoMovimentacaoUseCaseImpl(documentoDataProvider, sistemaDeArquivosIntegration);

        usecase.executar(new RemoverDocumentoMovimentacaoInputData(null, 1L));
    }

    @Test(expected = DocumentoNaoEncontradoException.class)
    public void deveFalharQuandoDocumentoNaoForEncontrado() {
        RemoverDocumentoMovimentacaoInputData inputData = RemoverDocumentoMovimentacaoInputData
            .builder()
            .id(1L)
            .movimentacao(1L)
            .build();

        RemoverDocumentoMovimentacaoUseCaseImpl usecase = new RemoverDocumentoMovimentacaoUseCaseImpl(documentoDataProvider, sistemaDeArquivosIntegration);

        Mockito.when(documentoDataProvider.buscarPorId(any(Long.class))).thenReturn(Optional.empty());

        usecase.executar(inputData);
    }

    @Test(expected = DocumentoNaoPertenceAMovimentacaoException.class)
    public void deveFalharQuandoDocumentoNaoPertencerAMovimentacao() {
        RemoverDocumentoMovimentacaoInputData inputData = RemoverDocumentoMovimentacaoInputData
            .builder()
            .id(1L)
            .movimentacao(1L)
            .build();

        RemoverDocumentoMovimentacaoUseCaseImpl usecase = new RemoverDocumentoMovimentacaoUseCaseImpl(documentoDataProvider, sistemaDeArquivosIntegration);

        Mockito.when(documentoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Documento.builder()
                .id(1L)
                .numero(anyString())
                .movimentacao(Movimentacao.builder().id(2L).build())
                .build()));

        usecase.executar(inputData);
    }

    @Test
    public void deveRemoverDocumento() {
        RemoverDocumentoMovimentacaoInputData inputData = new RemoverDocumentoMovimentacaoInputData();
        inputData.setId(1L);
        inputData.setMovimentacao(1L);

        RemoverDocumentoMovimentacaoUseCaseImpl usecase = new RemoverDocumentoMovimentacaoUseCaseImpl(documentoDataProvider, sistemaDeArquivosIntegration);

        Mockito.when(documentoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Documento.builder()
                .id(1L)
                .numero(anyString())
                .movimentacao(Movimentacao.builder().id(1L).build())
                .uriAnexo("uri")
                .build()));

        usecase.executar(inputData);

        Mockito.verify(documentoDataProvider, Mockito.times(1)).buscarPorId(any(Long.class));
        Mockito.verify(documentoDataProvider, Mockito.times(1)).remover(any(Long.class));
    }
}
