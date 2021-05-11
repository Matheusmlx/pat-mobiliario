package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento;


import br.com.azi.patrimoniomobiliario.domain.entity.Documento;
import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.exclusao.DeletarDocumentoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.exclusao.DeletarDocumentoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.exclusao.DeletarDocumentoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.exclusao.exception.DocumentoNaoEncontradoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.exclusao.exception.DocumentoNaoPertenceAIncorporacaoInformadaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.exclusao.exception.IncorporacaoEmProcessamentoException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DeletarDocumentoUseCaseTest {

    @Mock
    private DocumentoDataProvider documentoDataProvider;

    @Mock
    private IncorporacaoDataProvider incorporacaoDataProvider;

    @Mock
    private SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    private DeletarDocumentoUseCase useCase;

    @Before
    public void inicializa() {
        useCase = new DeletarDocumentoUseCaseImpl(documentoDataProvider,incorporacaoDataProvider,sistemaDeArquivosIntegration);

    }

    @Test
    public void deveDeletarDocumento() {
        DeletarDocumentoInputData inputData = new DeletarDocumentoInputData();
        inputData.setId(1L);
        inputData.setIncorporacao(1L);

        when(documentoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Documento.builder()
                .id(1L)
                .numero("123654")
                .incorporacao(Incorporacao.builder().id(1L).build())
                .uriAnexo("uri")
                .build()));

        when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Incorporacao.builder()
                .id(2L)
                .situacao(Incorporacao.Situacao.EM_ELABORACAO)
                .build()));

        useCase.executar(inputData);

        verify(documentoDataProvider, times(1)).buscarPorId(any(Long.class));
        verify(incorporacaoDataProvider, times(1)).buscarPorId(any(Long.class));
        verify(documentoDataProvider, times(1)).remover(any(Long.class));
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoIdForNulo() {
        useCase.executar(new DeletarDocumentoInputData());
    }

    @Test(expected = DocumentoNaoPertenceAIncorporacaoInformadaException.class)
    public void deveFalharQuandoDocumentoNaoForDoPatrimonioInformado() {
        DeletarDocumentoInputData inputData = DeletarDocumentoInputData
            .builder()
            .id(1L)
            .incorporacao(1L)
            .build();

        DeletarDocumentoUseCaseImpl usecase = new DeletarDocumentoUseCaseImpl(
            documentoDataProvider,
            incorporacaoDataProvider,
            sistemaDeArquivosIntegration
        );

        when(documentoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Documento.builder()
                .id(1L)
                .numero(anyString())
                .incorporacao(Incorporacao.builder().id(2L).build())
                .build()));

        usecase.executar(inputData);
    }

    @Test(expected = DocumentoNaoEncontradoException.class)
    public void deveFalharQuandoDocumentoNaoForEncontrado() {
        DeletarDocumentoInputData inputData = DeletarDocumentoInputData
            .builder()
            .id(1L)
            .incorporacao(1L)
            .build();

        DeletarDocumentoUseCaseImpl usecase = new DeletarDocumentoUseCaseImpl(
            documentoDataProvider,
            incorporacaoDataProvider,
            sistemaDeArquivosIntegration
        );

        when(documentoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.empty());

        usecase.executar(inputData);
    }

    @Test(expected = IncorporacaoEmProcessamentoException.class)
    public void deveFalharQuandoIncorporacaoEstiverEmProcessamento(){
        DeletarDocumentoInputData inputData = DeletarDocumentoInputData
            .builder()
            .id(1L)
            .incorporacao(3L)
            .build();

        when(documentoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Documento.builder()
                .id(1L)
                .numero("123456")
                .incorporacao(Incorporacao.builder().id(3L).build())
                .uriAnexo("uri")
                .build()));

        when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(
                Incorporacao
                    .builder()
                    .id(7L)
                    .codigo("2442-2")
                    .situacao(Incorporacao.Situacao.EM_PROCESSAMENTO)
                    .numProcesso("2223")
                    .build()
            ));

        useCase.executar(inputData);
    }
}
