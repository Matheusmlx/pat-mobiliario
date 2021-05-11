package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento;

import br.com.azi.patrimoniomobiliario.domain.entity.Arquivo;
import br.com.azi.patrimoniomobiliario.domain.entity.Documento;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.TipoDocumento;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.TipoDocumentoDataprovider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.edicao.EditarDocumentoMovimentacaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.edicao.EditarDocumentoMovimentacaoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.edicao.EditarDocumentoMovimentacaoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.edicao.EditarDocumentoMovimentacaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.edicao.converter.EditarDocumentoMovimentacaoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.edicao.exception.DocumentoJaCadastradoComMesmoNumeroETipoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.edicao.exception.DocumentoNaoEncontradoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.edicao.exception.TipoDocumentoNaoEncontradoException;
import br.com.azi.patrimoniomobiliario.utils.date.DateUtils;
import br.com.azi.patrimoniomobiliario.utils.validate.DateValidate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EditarDocumentoMovimentacaoUseCaseTest {

    @Mock
    private DocumentoDataProvider documentoDataProvider;

    @Mock
    private TipoDocumentoDataprovider tipoDocumentoDataprovider;

    @Mock
    private SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    private EditarDocumentoMovimentacaoUseCase useCase;

    @Before
    public void inicializar() {
        useCase = new EditarDocumentoMovimentacaoUseCaseImpl(
            documentoDataProvider,
            tipoDocumentoDataprovider,
            sistemaDeArquivosIntegration,
            new EditarDocumentoMovimentacaoOutputDataConverter()
        );
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoDocumentoIdForNulo() {
        useCase.executar(new EditarDocumentoMovimentacaoInputData());
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoMovimentacaoIdForNulo() {
        useCase.executar(EditarDocumentoMovimentacaoInputData.builder()
            .id(1L)
            .build());
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoNumeroForNulo() {
        useCase.executar(EditarDocumentoMovimentacaoInputData.builder()
            .id(1L)
            .movimentacao(1L)
            .build());
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoNumeroForVazio() {
        useCase.executar(EditarDocumentoMovimentacaoInputData.builder()
            .id(1L)
            .movimentacao(1L)
            .numero("")
            .build());
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoTipoIdForNulo() {
        useCase.executar(EditarDocumentoMovimentacaoInputData.builder()
            .id(1L)
            .movimentacao(1L)
            .numero("1234")
            .build());
    }

    @Test(expected = DocumentoNaoEncontradoException.class)
    public void deveFalharQuandoNaoEncontrarDocumentoPorId() {
        final Long documentoId = 1L;

        useCase.executar(EditarDocumentoMovimentacaoInputData.builder()
            .id(documentoId)
            .movimentacao(1L)
            .numero("1234")
            .tipo(1L)
            .uriAnexo("repo1:teste.pdf")
            .build());

        when(documentoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.empty());

        verify(documentoDataProvider, times(1)).buscarPorId(documentoId);
    }

    @Test(expected = TipoDocumentoNaoEncontradoException.class)
    public void deveFalharQuandoNaoEncontrarTipoDocumento() {
        final Long documentoId = 1L;
        final Long tipoDocumentoId = 1L;
        final Long novoTipoDocumentoId = 2L;

        when(documentoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(Documento.builder()
                .id(documentoId)
                .tipo(TipoDocumento.builder().id(tipoDocumentoId).build())
                .build()));

        when(tipoDocumentoDataprovider.buscarPorId(anyLong())).thenReturn(Optional.empty());

        useCase.executar(EditarDocumentoMovimentacaoInputData.builder()
            .id(documentoId)
            .movimentacao(1L)
            .numero("1234")
            .tipo(novoTipoDocumentoId)
            .uriAnexo("repo1:teste.pdf")
            .build());

        verify(tipoDocumentoDataprovider, times(1)).buscarPorId(novoTipoDocumentoId);
    }

    @Test(expected = DocumentoJaCadastradoComMesmoNumeroETipoException.class)
    public void deveFalharQuandoAtualizarOTipoEJaExistirOutroDocumentoComMesmoNumeroETipo() {
        final Long documentoId = 1L;
        final Long movimentacaoId = 1L;
        final Long tipoDocumentoId = 1L;
        final Long novoTipoDocumentoId = 2L;
        final String numeroDocumento = "1234";

        when(documentoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(Documento.builder()
                .id(documentoId)
                .numero(numeroDocumento)
                .tipo(TipoDocumento.builder().id(tipoDocumentoId).build())
                .movimentacao(Movimentacao.builder().id(movimentacaoId).build())
                .build()));

        when(tipoDocumentoDataprovider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(TipoDocumento.builder().id(novoTipoDocumentoId).build()));

        when(documentoDataProvider.existeDocumentoComNumeroETipoEMovimentacaoEdicao(anyLong(), anyLong(), anyString(), anyLong()))
            .thenReturn(true);

        useCase.executar(EditarDocumentoMovimentacaoInputData.builder()
            .id(documentoId)
            .movimentacao(movimentacaoId)
            .numero(numeroDocumento)
            .tipo(novoTipoDocumentoId)
            .uriAnexo("repo1:teste.pdf")
            .build());

        verify(documentoDataProvider, times(1))
            .existeDocumentoComNumeroETipoEMovimentacaoEdicao(documentoId, movimentacaoId, numeroDocumento, novoTipoDocumentoId);
    }

    @Test(expected = DocumentoJaCadastradoComMesmoNumeroETipoException.class)
    public void deveFalharQuandoAtualizarONumeroEJaExistirOutroDocumentoComMesmoNumeroETipo() {
        final Long documentoId = 1L;
        final Long movimentacaoId = 1L;
        final Long tipoDocumentoId = 1L;
        final String numeroDocumento = "1234";

        when(documentoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(Documento.builder()
                .id(documentoId)
                .numero("1111")
                .tipo(TipoDocumento.builder().id(tipoDocumentoId).build())
                .movimentacao(Movimentacao.builder().id(movimentacaoId).build())
                .build()));

        when(documentoDataProvider.existeDocumentoComNumeroETipoEMovimentacaoEdicao(anyLong(), anyLong(), anyString(), anyLong()))
            .thenReturn(true);

        useCase.executar(EditarDocumentoMovimentacaoInputData.builder()
            .id(documentoId)
            .movimentacao(movimentacaoId)
            .numero(numeroDocumento)
            .tipo(tipoDocumentoId)
            .uriAnexo("repo1:teste.pdf")
            .build());

        verifyZeroInteractions(tipoDocumentoDataprovider);

        verify(documentoDataProvider, times(1))
            .existeDocumentoComNumeroETipoEMovimentacaoEdicao(documentoId, movimentacaoId, numeroDocumento, tipoDocumentoId);
    }

    @Test
    public void deveAtualizarOsDadosDoDocumentoQuandoAsInformacoesEstiveremCorretas() {
        final Long documentoId = 1L;
        final Long movimentacaoId = 1L;
        final Long tipoDocumentoId = 1L;
        final Long novoTipoDocumentoId = 2L;
        final String novoNumeroDocumento = "1234";
        final String uriAnexo = "repo1:teste.pdf";
        final String novaUriAnexo = "repo1:teste1.pdf";
        final Date dataDocumento = new Date();

        final Documento documentoSalvoEsperado = Documento.builder()
            .id(documentoId)
            .tipo(TipoDocumento.builder().id(novoTipoDocumentoId).build())
            .movimentacao(Movimentacao.builder().id(movimentacaoId).build())
            .numero(novoNumeroDocumento)
            .data(DateUtils.asLocalDateTime(dataDocumento))
            .valor(new BigDecimal(100))
            .uriAnexo(novaUriAnexo)
            .build();

        final EditarDocumentoMovimentacaoOutputData outputDataEsperado = EditarDocumentoMovimentacaoOutputData
            .builder()
            .id(documentoId)
            .tipo(novoTipoDocumentoId)
            .movimentacao(movimentacaoId)
            .numero(novoNumeroDocumento)
            .data(DateValidate.formatarData(DateUtils.asLocalDateTime(dataDocumento)))
            .valor(new BigDecimal(100))
            .uriAnexo(novaUriAnexo)
            .build();

        when(documentoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(Documento.builder()
                .id(documentoId)
                .numero("1111")
                .uriAnexo(uriAnexo)
                .tipo(TipoDocumento.builder().id(tipoDocumentoId).build())
                .movimentacao(Movimentacao.builder().id(movimentacaoId).build())
                .build()));

        when(documentoDataProvider.existeDocumentoComNumeroETipoEMovimentacaoEdicao(anyLong(), anyLong(), anyString(), anyLong()))
            .thenReturn(false);

        when(tipoDocumentoDataprovider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(TipoDocumento.builder().id(novoTipoDocumentoId).build()));

        when(documentoDataProvider.salvar(any(Documento.class))).thenReturn(documentoSalvoEsperado);

        final EditarDocumentoMovimentacaoOutputData outputData = useCase.executar(EditarDocumentoMovimentacaoInputData
            .builder()
            .id(documentoId)
            .tipo(novoTipoDocumentoId)
            .movimentacao(movimentacaoId)
            .numero(novoNumeroDocumento)
            .data(dataDocumento)
            .valor(new BigDecimal(100))
            .uriAnexo(novaUriAnexo)
            .build());

        assertEquals(outputDataEsperado, outputData);

        verify(documentoDataProvider, times(1)).salvar(documentoSalvoEsperado);

        verify(sistemaDeArquivosIntegration, times(1))
            .removeDefinitiveFile(Arquivo.builder().uri(uriAnexo).build());

        verify(sistemaDeArquivosIntegration, times(1))
            .promote(Arquivo.builder().uri(documentoSalvoEsperado.getUriAnexo()).build());
    }

    @Test
    public void deveSerPossivelAtualizarDadosNulaveisQuandoPassarNull() {
        final Long documentoId = 1L;
        final Long movimentacaoId = 1L;
        final Long tipoDocumentoId = 1L;
        final Date dataDocumento = new Date();
        final String numero = "1234";
        final String uriAnexo = "repo1:teste.pdf";

        final Documento documentoSalvoEsperado = Documento.builder()
            .id(documentoId)
            .numero(numero)
            .uriAnexo(uriAnexo)
            .data(null)
            .valor(null)
            .tipo(TipoDocumento.builder().id(tipoDocumentoId).build())
            .movimentacao(Movimentacao.builder().id(movimentacaoId).build())
            .build();

        final EditarDocumentoMovimentacaoOutputData outputDataEsperado = EditarDocumentoMovimentacaoOutputData.builder()
            .id(documentoId)
            .numero(numero)
            .uriAnexo(uriAnexo)
            .data(null)
            .valor(null)
            .tipo(tipoDocumentoId)
            .movimentacao(movimentacaoId)
            .build();

        when(documentoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(Documento.builder()
                .id(documentoId)
                .numero(numero)
                .uriAnexo(uriAnexo)
                .data(DateUtils.asLocalDateTime(dataDocumento))
                .valor(new BigDecimal(100))
                .tipo(TipoDocumento.builder().id(tipoDocumentoId).build())
                .movimentacao(Movimentacao.builder().id(movimentacaoId).build())
                .build()));

        when(documentoDataProvider.salvar(any(Documento.class))).thenReturn(documentoSalvoEsperado);

        final EditarDocumentoMovimentacaoOutputData outputData = useCase.executar(EditarDocumentoMovimentacaoInputData
            .builder()
            .id(documentoId)
            .movimentacao(movimentacaoId)
            .tipo(tipoDocumentoId)
            .data(null)
            .valor(null)
            .numero(numero)
            .uriAnexo(uriAnexo)
            .build());

        assertEquals(outputDataEsperado, outputData);

        verifyZeroInteractions(tipoDocumentoDataprovider);
        verifyZeroInteractions(sistemaDeArquivosIntegration);
        verify(documentoDataProvider, times(1)).salvar(documentoSalvoEsperado);
    }
}
